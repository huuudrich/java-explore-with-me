package ru.practicum.huuudrich.service.admin;

import org.springframework.data.domain.Pageable;
import ru.practicum.huuudrich.model.category.CategoryDto;
import ru.practicum.huuudrich.model.compilations.CompilationDto;
import ru.practicum.huuudrich.model.compilations.NewCompilationDto;
import ru.practicum.huuudrich.model.compilations.UpdateCompilationRequest;
import ru.practicum.huuudrich.model.event.EventFullDto;
import ru.practicum.huuudrich.model.event.EventState;
import ru.practicum.huuudrich.model.event.UpdateEventAdminRequest;
import ru.practicum.huuudrich.model.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {
    List<UserDto> getAllUsers(List<Long> ids, Pageable pageable);

    UserDto createUser(UserDto userDto);

    void deleteUser(Long userId);

    CategoryDto addCategory(CategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);

    List<EventFullDto> searchEvents(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, Pageable pageable);

    EventFullDto eventDataUpdate(UpdateEventAdminRequest updateEvent, Long userId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilation);
}
