package ru.practicum.huuudrich.model.subscription;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.huuudrich.model.user.User;

import javax.persistence.*;

@Entity
@Table(name = "user_subscriptions", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    User followed;
}
