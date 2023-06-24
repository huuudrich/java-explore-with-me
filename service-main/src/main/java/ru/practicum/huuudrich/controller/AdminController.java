package ru.practicum.huuudrich.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.huuudrich.model.category.CategoryDto;
import ru.practicum.huuudrich.model.compilations.CompilationDto;
import ru.practicum.huuudrich.model.compilations.NewCompilationDto;
import ru.practicum.huuudrich.model.compilations.UpdateCompilationRequest;
import ru.practicum.huuudrich.model.event.EventFullDto;
import ru.practicum.huuudrich.model.event.EventState;
import ru.practicum.huuudrich.model.event.UpdateEventAdminRequest;
import ru.practicum.huuudrich.model.user.UserDto;
import ru.practicum.huuudrich.service.admin.AdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@Validated
public class AdminController {
    private static final String USER_PATH = "/users";
    private static final String CATEGORY_PATH = "/categories";
    private static final String EVENT_PATH = "/events";
    private static final String COMPILATION_PATH = "/compilations";
    private final AdminService adminService;

    @GetMapping(USER_PATH)
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                                                     @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                                     @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<UserDto> userDtoList = adminService.getAllUsers(ids, pageable);
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @PostMapping(USER_PATH)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = adminService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @DeleteMapping(USER_PATH + "/{userId}")
    public ResponseEntity<Void> deleteUser(@Positive @PathVariable Long userId) {
        adminService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CATEGORY_PATH)
    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = adminService.addCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @DeleteMapping(CATEGORY_PATH + "/{catId}")
    public ResponseEntity<Void> deleteCategory(@Positive @PathVariable Long catId) {
        adminService.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(CATEGORY_PATH + "/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@Positive @PathVariable Long catId,
                                                      @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto updateCategory = adminService.updateCategory(catId, categoryDto);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);

    }

    @GetMapping(EVENT_PATH)
    public ResponseEntity<List<EventFullDto>> searchEvent(@RequestParam(value = "users", required = false) List<Long> users,
                                                          @RequestParam(value = "states", required = false) List<EventState> states,
                                                          @RequestParam(value = "categories", required = false) List<Long> categories,
                                                          @RequestParam(value = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                          @RequestParam(value = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                          @RequestParam(value = "from", defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                          @RequestParam(value = "size", defaultValue = "10", required = false) @Positive Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "views"));
        List<EventFullDto> eventFullDtoList = adminService.searchEvents(users, states, categories, rangeStart, rangeEnd, pageable);
        return new ResponseEntity<>(eventFullDtoList, HttpStatus.OK);
    }

    @PatchMapping(EVENT_PATH + "/{eventId}")
    public ResponseEntity<EventFullDto> eventDataUpdate(@RequestBody @Valid UpdateEventAdminRequest updateEvent, @PathVariable @Positive Long eventId) {
        EventFullDto eventFullDto = adminService.eventDataUpdate(updateEvent, eventId);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @PostMapping(COMPILATION_PATH)
    public ResponseEntity<CompilationDto> addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        CompilationDto createdCompilation = adminService.addCompilation(newCompilationDto);
        return new ResponseEntity<>(createdCompilation, HttpStatus.CREATED);
    }

    @DeleteMapping(COMPILATION_PATH + "/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable @Positive Long compId) {
        adminService.deleteCompilation(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(COMPILATION_PATH + "/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable @Positive Long compId,
                                                            @RequestBody @Valid UpdateCompilationRequest updateCompilation) {
        CompilationDto compilationDto = adminService.updateCompilation(compId, updateCompilation);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }
}
