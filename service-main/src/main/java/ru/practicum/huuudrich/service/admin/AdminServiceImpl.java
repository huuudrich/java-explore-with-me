package ru.practicum.huuudrich.service.admin;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.huuudrich.mapper.CategoryMapper;
import ru.practicum.huuudrich.mapper.CompilationMapper;
import ru.practicum.huuudrich.mapper.EventMapper;
import ru.practicum.huuudrich.mapper.UserMapper;
import ru.practicum.huuudrich.model.category.Category;
import ru.practicum.huuudrich.model.category.CategoryDto;
import ru.practicum.huuudrich.model.compilations.Compilation;
import ru.practicum.huuudrich.model.compilations.CompilationDto;
import ru.practicum.huuudrich.model.compilations.NewCompilationDto;
import ru.practicum.huuudrich.model.compilations.UpdateCompilationRequest;
import ru.practicum.huuudrich.model.event.*;
import ru.practicum.huuudrich.model.user.User;
import ru.practicum.huuudrich.model.user.UserDto;
import ru.practicum.huuudrich.repository.CategoryRepository;
import ru.practicum.huuudrich.repository.CompilationRepository;
import ru.practicum.huuudrich.repository.EventRepository;
import ru.practicum.huuudrich.repository.UserRepository;
import ru.practicum.huuudrich.utils.CopyNonNullProperties;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, Pageable pageable) {
        log.info("Getting users");
        List<User> users;
        if (ids != null) {
            users = new ArrayList<>(userRepository.getAllByIdIn(ids, pageable));
        } else {
            users = new ArrayList<>(userRepository.findAll(pageable).getContent());
        }

        return UserMapper.INSTANCE.toListDto(users);
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        log.info(String.format("Create user with email: %s", userDto.getEmail()));
        User user = UserMapper.INSTANCE.toEntity(userDto);
        userRepository.save(user);
        return UserMapper.INSTANCE.toDto(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        log.info(String.format("Delete user with id: %s", userId));
        userRepository.deleteById(userId);
    }


    @Transactional
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        log.info(String.format("Add category with name: %s", categoryDto.getName()));
        Category category = CategoryMapper.INSTANCE.toEntity(categoryDto);
        categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toDto(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        categoryDto.setId(categoryId);
        Category category = CategoryMapper.INSTANCE.toEntity(categoryDto);
        categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toDto(category);
    }

    @Override
    public List<EventFullDto> searchEvents(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Pageable pageable) {
        log.info("Search events");

        QEvent event = QEvent.event;
        BooleanBuilder whereClause = new BooleanBuilder();

        if (users != null && !users.isEmpty()) {
            whereClause.and(event.initiator.id.in(users));
        }

        if (categories != null && !categories.isEmpty()) {
            whereClause.and(event.category.id.in(categories));
        }

        if (states != null && !states.isEmpty()) {
            whereClause.and(event.state.in(states));
        }
        if (rangeStart != null) {
            whereClause.and(event.eventDate.goe(rangeStart));
        }
        if (rangeEnd != null) {
            whereClause.and(event.eventDate.loe(rangeEnd));
        }

        List<Event> events = new ArrayList<>(eventRepository.findAll(whereClause, pageable).getContent());

        return EventMapper.INSTANCE.toListFullDto(events);
    }

    @Transactional
    @Override
    public EventFullDto eventDataUpdate(UpdateEventAdminRequest updateEventRequest, Long eventId) {
        Event event = getEvent(eventId);
        Event updateEvent = EventMapper.INSTANCE.updateEventToEventAdmin(updateEventRequest);


        event.setPublishedOn(LocalDateTime.now());

        LocalDateTime minTime = event.getPublishedOn().plusHours(1);
        if (event.getEventDate().isBefore(minTime) || (updateEvent.getEventDate() != null &&
                updateEvent.getEventDate().isBefore(minTime))) {
            throw new DataIntegrityViolationException("The start date of the event to be changed must be no earlier than\n" +
                    "than one hour from the date of publication");
        }
        if (event.getState() != EventState.PENDING) {
            throw new DataIntegrityViolationException("An event can only be published if it is in a state of pending publication");
        }
        CopyNonNullProperties.copy(updateEvent, event);

        if (updateEventRequest.getStateAction() == StateActionAdm.PUBLISH_EVENT) {
            event.setState(EventState.PUBLISHED);
        } else if (updateEventRequest.getStateAction() == StateActionAdm.REJECT_EVENT) {
            event.setState(EventState.CANCELED);
        }
        if (updateEventRequest.getCategory() != null) {
            setUpCategory(event, updateEventRequest.getCategory());
        }

        Event newEvent = eventRepository.save(event);

        return EventMapper.INSTANCE.toFullDto(newEvent);
    }

    @Transactional
    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.INSTANCE.toCompilation(newCompilationDto);
        List<Event> events = new ArrayList<>(eventRepository.getAllByIdIn(newCompilationDto.getEvents()));
        compilation.setEvents(events);
        CompilationDto compilationDto = CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
        compilationDto.setEvents(EventMapper.INSTANCE.toShortDtoList(events));
        return compilationDto;
    }

    @Override
    public void deleteCompilation(Long compId) {
        log.info(String.format("Delete compilation with id: %s", compId));
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilation) {
        Compilation compilation = CompilationMapper.INSTANCE.toCompilation(updateCompilation);
        Compilation oldCompilation = getCompilation(compId);

        CopyNonNullProperties.copy(oldCompilation, compilation);

        compilation.setEvents(new ArrayList<>(eventRepository.getAllByIdIn(updateCompilation.getEvents())));
        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event not found with id: %d", eventId)));
    }

    private Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Compilation not found with id: %d", compId)));
    }

    private Event setUpCategory(Event event, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category not found with id: %d", categoryId)));
        event.setCategory(category);
        return event;
    }
}
