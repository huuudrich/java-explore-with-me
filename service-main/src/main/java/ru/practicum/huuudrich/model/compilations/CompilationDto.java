package ru.practicum.huuudrich.model.compilations;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.huuudrich.model.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    Long id;
    List<EventShortDto> events;
    Boolean pinned;
    @NotBlank(message = "Title compilations cannot be blank")
    @Size(min = 1, max = 50, message = "Title compilations should be between 1 and 50 characters")
    String title;
}
