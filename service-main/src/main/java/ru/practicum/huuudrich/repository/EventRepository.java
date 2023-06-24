package ru.practicum.huuudrich.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.huuudrich.model.event.Event;
import ru.practicum.huuudrich.model.event.EventState;
import ru.practicum.huuudrich.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> getAllByInitiator(User initiator, Pageable pageable);

    Optional<Event> getEventByIdAndInitiatorId(Long EventId, Long initiatorId);

    @Modifying
    @Query("update Event e set e.confirmedRequests = e.confirmedRequests + 1 where e.id = :eventId")
    void incrementConfirmedRequests(@Param("eventId") Long eventId);

    @Modifying
    @Query("update Event e set e.views = e.views + 1 where e.id = :eventId")
    void incrementViews(@Param("eventId") Long eventId);

    @Modifying
    @Query("update Event e set e.views = e.views + 1 where e IN :events")
    void incrementViewsList(@Param("events") List<Event> events);

    @Modifying
    @Query("update Event e set e.confirmedRequests = e.confirmedRequests - 1 where e.id = :eventId")
    void decrementConfirmedRequests(@Param("eventId") Long eventId);

    @Modifying
    @Query("update Event e set e.views = e.views - 1 where e.id = :eventId")
    void decrementViews(@Param("eventId") Long eventId);

    List<Event> getAllByIdIn(List<Long> ids);

    Optional<Event> findByIdAndStateIsContaining(Long id, EventState eventState);

    List<Event> findByInitiator(User initiator);

    List<Event> findByInitiatorIn(List<User> initiators);
}
