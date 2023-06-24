package ru.practicum.huuudrich.model.subscription;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
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
    @Builder.Default
    List<Long> followers = new ArrayList<>();
    @Builder.Default
    List<Long> following = new ArrayList<>();
    @Builder.Default
    Boolean allowSubscriptions = true;
}
