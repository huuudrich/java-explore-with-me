package ru.practicum.huuudrich.model.compilations;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {
    Long id;
    @Builder.Default
    List<Long> events = new ArrayList<>();
    Boolean pinned;
    @Size(min = 1, max = 50, message = "Title compilations should be between 1 and 50 characters")
    String title;
}
