package ru.practicum.huuudrich.model.compilations;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {
    Long id;
    @Builder.Default
    List<Long> events = new ArrayList<>();
    Boolean pinned;
    @NotBlank(message = "Title compilations cannot be blank")
    @Size(min = 1, max = 50, message = "Title compilations should be between 1 and 50 characters")
    String title;
}
