package ru.practicum.huuudrich.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.huuudrich.model.subscription.UserSubscription;
import ru.practicum.huuudrich.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionsRepository extends JpaRepository<UserSubscription, Long> {
    Optional<UserSubscription> findByFollowerAndFollowed(User user, User subscribedUser);

    Optional<UserSubscription> findByFollowerIdAndFollowedId(Long userId, Long subscribedId);

    List<UserSubscription> findByFollower(User user);
}
