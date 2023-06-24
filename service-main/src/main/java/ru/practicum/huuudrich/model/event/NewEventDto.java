package ru.practicum.huuudrich.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.huuudrich.model.event.Event.Location;
import ru.practicum.huuudrich.utils.annotation.DateNotPast;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    Long id;

    @NotBlank(message = "Annotation cannot be blank")
    @Size(min = 20, max = 2000, message = "Annotation should be between 20 and 2000 characters")
    String annotation;

    @Positive
    Long category;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 20, max = 7000, message = "Description should be between 20 and 7000 characters")
    String description;

    @NotNull
    @DateNotPast(message = "must contain a date that has not yet occurred")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @NotNull
    Location location;
    @Builder.Default
    Boolean paid = false;
    @Builder.Default
    Long participantLimit = 0L;
    @Builder.Default
    Boolean requestModeration = true;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 120, message = "Title should be between 50 and 120 characters")
    String title;
}
