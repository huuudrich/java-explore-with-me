package ru.practicum.huuudrich.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {
    Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
    @NotNull
    Long event;
    @NotNull
    Long requester;
    @Enumerated(EnumType.STRING)
    RequestStatus status;

    public ParticipationRequestDto(Long id, LocalDateTime created, Long event, Long requester, RequestStatus status) {
        this.id = id;
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
}
