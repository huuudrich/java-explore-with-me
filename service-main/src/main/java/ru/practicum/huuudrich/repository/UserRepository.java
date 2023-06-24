package ru.practicum.huuudrich.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.huuudrich.model.user.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getAllByIdIn(List<Long> ids, Pageable pageable);
    @Query("SELECT us.followed.id FROM UserSubscription us WHERE us.follower = :user")
    List<Long> getFollowedIdsByUser(@Param("user") User user);
    @Query("SELECT us.follower.id FROM UserSubscription us WHERE us.followed = :user")
    List<Long> getSubcriptionsIdsByUser(@Param("user") User user);
}
