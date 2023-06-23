package ru.practicum.huuudrich.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.huuudrich.utils.annotation.DateNotPast;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventUserRequest {
    Long id;

    @Size(min = 20, max = 2000, message = "Annotation should be between 20 and 2000 characters")
    String annotation;

    @Positive
    Long category;

    @Size(min = 20, max = 7000, message = "Description should be between 20 and 7000 characters")
    String description;

    @DateNotPast(message = "must contain a date that has not yet occurred")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    Event.Location location;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    StateActionUsr stateAction;
    @Size(min = 3, max = 120, message = "Title should be between 20 and 2000 characters")
    String title;
}
