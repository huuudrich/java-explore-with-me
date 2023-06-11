package ru.practicum.huuudrich.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientRequest {
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
