package ru.practicum.huuudrich.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.huuudrich.model.category.CategoryDto;
import ru.practicum.huuudrich.model.compilations.CompilationDto;
import ru.practicum.huuudrich.model.event.EventFullDto;
import ru.practicum.huuudrich.model.event.EventShortDto;
import ru.practicum.huuudrich.service.publicapi.PublicApiService;
import ru.practicum.huuudrich.utils.exception.BadRequestException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
public class PublicApiController {
    private static final String CATEGORY_PATH = "/categories";
    private static final String EVENT_PATH = "/events";
    private static final String COMPILATION_PATH = "/compilations";
    private final PublicApiService publicService;

    @GetMapping(COMPILATION_PATH)
    public ResponseEntity<List<CompilationDto>> getAllCompilations(@RequestParam(value = "pinned", required = false, defaultValue = "false") Boolean pinned,
                                                                   @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                                                   @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "id"));
        List<CompilationDto> compilationDtoList = publicService.getAllCompilations(pinned, pageable);
        return new ResponseEntity<>(compilationDtoList, HttpStatus.OK);
    }

    @GetMapping(COMPILATION_PATH + "/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable @Positive Long compId) {
        CompilationDto compilationDto = publicService.getCompilation(compId);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }

    @GetMapping(CATEGORY_PATH)
    public ResponseEntity<List<CategoryDto>> getAllCategories(@RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                                              @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "id"));
        List<CategoryDto> categoryDtoList = publicService.getAllCategories(pageable);
        return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);
    }

    @GetMapping(CATEGORY_PATH + "/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable @Positive Long catId) {
        CategoryDto categoryDto = publicService.getCategory(catId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @GetMapping(EVENT_PATH)
    public ResponseEntity<List<EventShortDto>> getAllEvents(@RequestParam(value = "text", required = false) String text,
                                                            @RequestParam(value = "categories", required = false) List<Long> categories,
                                                            @RequestParam(value = "paid", required = false) Boolean paid,
                                                            @RequestParam(value = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                            @RequestParam(value = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                            @RequestParam(value = "onlyAvailable", defaultValue = "false", required = false) Boolean onlyAvailable,
                                                            @RequestParam(value = "sort", required = false) String sort,
                                                            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                                            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) throws BadRequestException {
        Sort sorting;
        if (sort == null || sort.equals("EVENT_DATE")) {
            sorting = Sort.by(Sort.Direction.DESC, "eventDate");
        } else if (sort.equals("VIEWS")) {
            sorting = Sort.by(Sort.Direction.DESC, "views");
        } else {
            throw new IllegalArgumentException("Invalid sort parameter");
        }

        Pageable pageable = PageRequest.of(from / size, size, sorting);

        List<EventShortDto> eventShortDtoList = publicService.getAllEvents(text, paid, categories, rangeStart, rangeEnd, onlyAvailable, pageable);

        return new ResponseEntity<>(eventShortDtoList, HttpStatus.OK);
    }

    @GetMapping(EVENT_PATH + "/{id}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable @Positive Long id, HttpServletRequest request) {
        EventFullDto eventFullDto = publicService.getEvent(id, request);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }
}
