package ru.practicum.huuudrich.service.user;

import org.springframework.data.domain.Pageable;
import ru.practicum.huuudrich.model.event.*;
import ru.practicum.huuudrich.model.request.*;

import java.util.List;

public interface UserService {
    List<EventShortDto> getEventsByCurrentUser(Long userId, Pageable pageable);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventsByUserAndEvent(Long userId, Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getRequestsByInitiator(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<ParticipationRequestDto> getRequestsByUser(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
