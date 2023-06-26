package ru.practicum.huuudrich.model.subscription;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSubscribeDto {
    Long id;
    String name;
    List<Long> followers;
    List<Long> following;
    @Builder.Default
    Boolean allowSubscriptions = true;
}
