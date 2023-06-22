package ru.practicum.huuudrich.service.user;

import org.springframework.data.domain.Pageable;
import ru.practicum.huuudrich.model.event.EventFullDto;
import ru.practicum.huuudrich.model.event.EventShortDto;
import ru.practicum.huuudrich.model.event.NewEventDto;
import ru.practicum.huuudrich.model.event.UpdateEventUserRequest;
import ru.practicum.huuudrich.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.huuudrich.model.request.EventRequestStatusUpdateResult;
import ru.practicum.huuudrich.model.request.ParticipationRequestDto;
import ru.practicum.huuudrich.utils.exception.EventStateException;

import java.util.List;

public interface UserService {
    List<EventShortDto> getEventsByCurrentUser(Long userId, Pageable pageable);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventsByUserAndEvent(Long userId, Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) throws EventStateException;

    List<ParticipationRequestDto> getRequestsByInitiator(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<ParticipationRequestDto> getRequestsByUser(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    void deleteRequest(Long userId, Long requestId);
}
