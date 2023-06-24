package ru.practicum.huuudrich.service.publicapi;

import org.springframework.data.domain.Pageable;
import ru.practicum.huuudrich.model.category.CategoryDto;
import ru.practicum.huuudrich.model.compilations.CompilationDto;
import ru.practicum.huuudrich.model.event.EventFullDto;
import ru.practicum.huuudrich.model.event.EventShortDto;
import ru.practicum.huuudrich.utils.exception.BadRequestException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicApiService {
    List<CompilationDto> getAllCompilations(Boolean pinned, Pageable pageable);

    CompilationDto getCompilation(Long compId);

    List<CategoryDto> getAllCategories(Pageable pageable);

    CategoryDto getCategory(Long catId);

    List<EventShortDto> getAllEvents(String text, Boolean paid, List<Long> categories, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable) throws BadRequestException;

    EventFullDto getEvent(Long eventId, HttpServletRequest request);
}
