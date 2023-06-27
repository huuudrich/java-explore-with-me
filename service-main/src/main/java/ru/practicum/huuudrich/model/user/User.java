package ru.practicum.huuudrich.model.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.huuudrich.model.subscription.UserSubscription;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @Column(unique = true)
    String email;
    @OneToMany(mappedBy = "follower")
    List<UserSubscription> followers = new ArrayList<>();
    @OneToMany(mappedBy = "followed")
    List<UserSubscription> following = new ArrayList<>();
    @Builder.Default
    @JoinColumn(name = "allow_subscriptions")
    Boolean allowSubscriptions = true;
}
