package ru.practicum.huuudrich.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequest {
    @Builder.Default
    List<Long> requestIds = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    RequestStatus status;
}
