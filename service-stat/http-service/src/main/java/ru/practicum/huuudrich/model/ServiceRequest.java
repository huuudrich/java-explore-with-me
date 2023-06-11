package ru.practicum.huuudrich.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Builder
@Entity
@Table(name = "requests",schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @JsonProperty("app")
    String app;
    @JsonProperty("uri")
    String uri;
    @JsonProperty("ip")
    String ip;
    @JsonProperty("timestamp")
    LocalDateTime timestamp;
}
