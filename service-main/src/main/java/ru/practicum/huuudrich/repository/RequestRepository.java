package ru.practicum.huuudrich.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.huuudrich.model.event.Event;
import ru.practicum.huuudrich.model.request.ParticipationRequestDto;
import ru.practicum.huuudrich.model.request.Request;
import ru.practicum.huuudrich.model.request.RequestStatus;
import ru.practicum.huuudrich.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> getAllByRequester(User requester);

    Optional<Request> findByRequesterAndEvent(User requester, Event event);

    List<Request> getAllByEvent(Event event);

    List<Request> getAllByIdInAndEvent(List<Long> ids, Event event);

    List<Request> findByEventIdAndStatus(Long eventId, RequestStatus status);

    @Query("SELECT new ru.practicum.huuudrich.model.request.ParticipationRequestDto(r.id, r.created, r.event.id, r.requester.id,r.status)" +
            "FROM Request r WHERE r.id = :requestId")
    Optional<ParticipationRequestDto> getParticipationRequestDto(Long requestId);

    @Query("SELECT new ru.practicum.huuudrich.model.request.ParticipationRequestDto(r.id, r.created, r.event.id, r.requester.id,r.status)" +
            "FROM Request r WHERE r IN :requests AND r.status = :status")
    List<ParticipationRequestDto> getListParticipationRequestDto(List<Request> requests, RequestStatus status);
}
