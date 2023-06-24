package ru.practicum.huuudrich.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    @Enumerated(EnumType.STRING)
    RequestStatus status;

    @PrePersist
    public void prePersist() {
        if (this.requestIds == null) {
            this.requestIds = new ArrayList<>();
        }
    }
}
