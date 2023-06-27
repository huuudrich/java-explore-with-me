package ru.practicum.huuudrich.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.huuudrich.mapper.*;
import ru.practicum.huuudrich.model.category.Category;
import ru.practicum.huuudrich.model.event.*;
import ru.practicum.huuudrich.model.request.*;
import ru.practicum.huuudrich.model.user.User;
import ru.practicum.huuudrich.repository.CategoryRepository;
import ru.practicum.huuudrich.repository.EventRepository;
import ru.practicum.huuudrich.repository.RequestRepository;
import ru.practicum.huuudrich.repository.UserRepository;
import ru.practicum.huuudrich.utils.CopyNonNullProperties;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    @Transactional
    @Override
    public List<EventShortDto> getEventsByCurrentUser(Long userId, Pageable pageable) {
        User user = getUser(userId);
        List<Event> events = new ArrayList<>(eventRepository.getAllByInitiator(user, pageable));

        log.info(String.format("Get array of events with user id: %s", userId));

        return EventMapper.INSTANCE.toShortDtoList(events);
    }

    @Transactional
    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        User user = getUser(userId);
        Event event = setUpCategory(EventMapper.INSTANCE.newEventDtoToEvent(newEventDto), newEventDto.getCategory());

        log.info(String.format("Create event with user id: %s", userId));

        event.setInitiator(user);
        return EventMapper.INSTANCE.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventsByUserAndEvent(Long userId, Long eventId) {
        Event event = getEvent(userId, eventId);
        return EventMapper.INSTANCE.toFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event eventDb = getEvent(userId, eventId);
        Event event = EventMapper.INSTANCE.updateEventToEventRequest(updateEventUserRequest);

        log.info(String.format("Update status eventId: %s", eventId));

        if (eventDb.getState() == EventState.PUBLISHED) {
            throw new DataIntegrityViolationException("Event state must be 'PENDING' or 'CANCELED'");
        }

        CopyNonNullProperties.copy(event, eventDb);


        if (updateEventUserRequest.getStateAction() == StateActionUsr.CANCEL_REVIEW) {
            eventDb.setState(EventState.CANCELED);
        }
        if (updateEventUserRequest.getStateAction() == StateActionUsr.SEND_TO_REVIEW) {
            eventDb.setState(EventState.PENDING);
        }

        if (updateEventUserRequest.getCategory() != null) {
            setUpCategory(eventDb, updateEventUserRequest.getCategory());
        }

        Event updateEvent = eventRepository.save(eventDb);

        return EventMapper.INSTANCE.toFullDto(updateEvent);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByInitiator(Long userId, Long eventId) {
        getUser(userId);
        Event event = getEvent(userId, eventId);
        List<Request> requests = new ArrayList<>(requestRepository.getAllByEvent(event));
        return RequestMapper.INSTANCE.listToRequestDto(requests);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        Event event = getEvent(userId, eventId);

        Long limit = event.getParticipantLimit();

        if (limit == 0 || !event.getRequestModeration()) {
            throw new DataIntegrityViolationException("The requests always confirmed");
        } else if (event.getConfirmedRequests() >= limit) {
            throw new DataIntegrityViolationException("The request limit has been reached");
        }

        List<Request> requests = new ArrayList<>(requestRepository.getAllByIdInAndEvent(request.getRequestIds(), event));

        if (requests.isEmpty()) {
            throw new DataIntegrityViolationException("Requests is empty");
        }

        for (Request re : requests) {
            if (re.getStatus() != RequestStatus.PENDING) {
                throw new DataIntegrityViolationException("Status not a 'PENDING'");
            }
            if (event.getConfirmedRequests() > event.getParticipantLimit()) {
                re.setStatus(RequestStatus.REJECTED);
            } else {
                re.setStatus(request.getStatus());
                if (request.getStatus() == RequestStatus.CONFIRMED) {
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                }
            }
        }

        requestRepository.saveAll(requests);
        eventRepository.save(event);

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>(setUpListEvents(requests, RequestStatus.CONFIRMED));
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>(setUpListEvents(requests, RequestStatus.REJECTED));

        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);

        return result;
    }

    //requests
    @Override
    public List<ParticipationRequestDto> getRequestsByUser(Long userId) {
        User requester = getUser(userId);
        List<Request> requests = new ArrayList<>(requestRepository.getAllByRequester(requester));
        return RequestMapper.INSTANCE.listToRequestDto(requests);
    }

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User requester = getUser(userId);
        Event event = getEvent(eventId);
        Request request = new Request();

        Long initiatorId = event.getInitiator().getId();
        Optional<Request> existingRequest = requestRepository.findByRequesterAndEvent(requester, event);

        Long limit = event.getParticipantLimit();
        Long confirmedRequests = event.getConfirmedRequests();


        if (existingRequest.isPresent()) {
            throw new DataIntegrityViolationException("The request already exists");
        } else if (Objects.equals(initiatorId, userId)) {
            throw new DataIntegrityViolationException("The initiator cannot send a request");
        } else if (event.getState() != EventState.PUBLISHED) {
            throw new DataIntegrityViolationException("Event not published");
        }

        if (limit != 0 && confirmedRequests >= limit) {
            throw new DataIntegrityViolationException("The request limit has been reached");
        }
        if (limit == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            eventRepository.incrementConfirmedRequests(eventId);
        }

        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            eventRepository.incrementConfirmedRequests(eventId);
        }

        request.setRequester(requester);
        request.setEvent(event);

        request = requestRepository.save(request);

        return getRequestDto(request.getId());
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        getUser(userId);
        Request request = getRequest(requestId);
        Event event = request.getEvent();

        if (request.getStatus() == RequestStatus.CONFIRMED) {
            eventRepository.decrementConfirmedRequests(event.getId());
        }

        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.INSTANCE.toRequestDto(request);
    }

    private List<ParticipationRequestDto> setUpListEvents(List<Request> requests, RequestStatus state) {
        return requestRepository.getListParticipationRequestDto(requests, state);
    }

    private ParticipationRequestDto getRequestDto(Long requestId) {
        return requestRepository.getParticipationRequestDto(requestId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Request not found with id: %d", requestId)));
    }

    private Event getEvent(Long userId, Long eventId) {
        return eventRepository.getEventByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new DataIntegrityViolationException(String.format("User not found with id: %d and eventId: %d", userId, eventId)));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event not found with id: %d", eventId)));
    }

    private Request getRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Request not found with id: %d", requestId)));
    }

    private Event setUpCategory(Event event, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category not found with id: %d", categoryId)));
        event.setCategory(category);
        return event;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User not found with id: %d", userId)));
    }
}
