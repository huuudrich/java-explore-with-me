package ru.practicum.huuudrich.service.publicapi;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.huuudrich.client.ClientRequest;
import ru.practicum.huuudrich.client.StatsClient;
import ru.practicum.huuudrich.mapper.CategoryMapper;
import ru.practicum.huuudrich.mapper.CompilationMapper;
import ru.practicum.huuudrich.mapper.EventMapper;
import ru.practicum.huuudrich.model.category.Category;
import ru.practicum.huuudrich.model.category.CategoryDto;
import ru.practicum.huuudrich.model.compilations.Compilation;
import ru.practicum.huuudrich.model.compilations.CompilationDto;
import ru.practicum.huuudrich.model.event.*;
import ru.practicum.huuudrich.repository.CategoryRepository;
import ru.practicum.huuudrich.repository.CompilationRepository;
import ru.practicum.huuudrich.repository.EventRepository;
import ru.practicum.huuudrich.utils.exception.BadRequestException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PublicApiServiceImpl implements PublicApiService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final StatsClient statsClient;

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations = new ArrayList<>(compilationRepository.getAllByPinnedIs(pinned, pageable));
        return compilations.stream().map(compilation -> {

            CompilationDto dto = CompilationMapper.INSTANCE.toCompilationDto(compilation);

            List<EventShortDto> eventsShort = EventMapper.INSTANCE.toShortDtoList(compilation.getEvents());
            dto.setEvents(eventsShort);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Compilation not found with id: %d", compId)));
        CompilationDto compilationDto = CompilationMapper.INSTANCE.toCompilationDto(compilation);
        compilationDto.setEvents(EventMapper.INSTANCE.toShortDtoList(compilation.getEvents()));
        return compilationDto;
    }

    @Override
    public List<CategoryDto> getAllCategories(Pageable pageable) {
        List<Category> categories = new ArrayList<>(categoryRepository.findAll(pageable).getContent());
        return CategoryMapper.INSTANCE.toListCategoryDto(categories);
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category not found with id: %d", catId)));
        return CategoryMapper.INSTANCE.toDto(category);
    }

    @Transactional
    @Override
    public List<EventShortDto> getAllEvents(String text, Boolean paid, List<Long> categories, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable, HttpServletRequest request) throws BadRequestException {
        QEvent qEvent = QEvent.event;

        BooleanBuilder whereClause = new BooleanBuilder();

        if (rangeStart == null && rangeEnd == null) {
            whereClause.and(qEvent.eventDate.after(LocalDateTime.now()));
        }
        if (rangeStart != null) {
            whereClause.and(qEvent.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            whereClause.and(qEvent.eventDate.before(rangeEnd));
        }

        if (rangeStart != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException("RangeStart is after rangeEnd");
            }
        }

        if (text != null) {
            whereClause.and(qEvent.annotation.containsIgnoreCase(text).or(qEvent.description.containsIgnoreCase(text)));
        }

        if (paid != null) {
            whereClause.and(qEvent.paid.eq(paid));
        }

        if (categories != null && !categories.isEmpty()) {
            whereClause.and(qEvent.category.id.in(categories));
        }


        if (onlyAvailable) {
            whereClause.and(qEvent.confirmedRequests.lt(qEvent.participantLimit));
        }

        List<Event> events = eventRepository.findAll(whereClause, pageable).getContent();
        ClientRequest clientRequest = createClientRequest(request);
        log.info(statsClient.saveRequest(clientRequest).toString());

        if (!statsClient.checkIp(clientRequest.getIp(), clientRequest.getUri())) {
            eventRepository.incrementViewsList(events);
        }

        return EventMapper.INSTANCE.toShortDtoList(events);
    }

    @Transactional
    @Override
    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event not found with id: %d", eventId)));
        if (event.getState() != EventState.PUBLISHED) {
            throw new EntityNotFoundException(String.format("Event not found with id: %d", eventId));
        }
        ClientRequest clientRequest = createClientRequest(request);


        if (!statsClient.checkIp(clientRequest.getIp(), clientRequest.getUri())) {
            eventRepository.incrementViews(eventId);
        }

        log.info(statsClient.saveRequest(clientRequest).toString());

        return EventMapper.INSTANCE.toFullDto(event);
    }

    private ClientRequest createClientRequest(HttpServletRequest request) {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setApp("service-main");
        clientRequest.setTimestamp(LocalDateTime.now());
        clientRequest.setIp(request.getRemoteAddr());
        clientRequest.setUri(request.getRequestURI());
        return clientRequest;
    }

}
