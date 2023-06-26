package ru.practicum.huuudrich.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.huuudrich.model.event.*;
import ru.practicum.huuudrich.model.request.*;
import ru.practicum.huuudrich.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController {
    private static final String EVENTS_PATH = "/events";
    private static final String REQUEST_PATH = "/requests";
    private final UserService userService;

    @GetMapping("{userId}" + EVENTS_PATH)
    public ResponseEntity<List<EventShortDto>> getEventsByCurrentUser(@PathVariable @Positive Long userId,
                                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0", required = false) Integer from,
                                                                      @PositiveOrZero @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "views"));
        List<EventShortDto> events = userService.getEventsByCurrentUser(userId, pageable);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("{userId}" + EVENTS_PATH)
    public ResponseEntity<EventFullDto> createEvent(@PathVariable @Positive Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        EventFullDto createdEvent = userService.createEvent(userId, newEventDto);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("{userId}" + EVENTS_PATH + "/{eventId}")
    public ResponseEntity<EventFullDto> getEventByUserAndEvent(@PathVariable @Positive Long userId, @PathVariable @Positive Long eventId) {
        EventFullDto eventFullDto = userService.getEventsByUserAndEvent(userId, eventId);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @PatchMapping("{userId}" + EVENTS_PATH + "/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByUser(@PathVariable @Positive Long userId, @PathVariable @Positive Long eventId,
                                                          @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        EventFullDto eventFullDto = userService.updateEventByUser(userId, eventId, updateEventUserRequest);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    //requests
    @GetMapping("{userId}" + REQUEST_PATH)
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsByUser(@PathVariable @Positive Long userId) {
        List<ParticipationRequestDto> requests = userService.getRequestsByUser(userId);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PostMapping("{userId}" + REQUEST_PATH)
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable @Positive Long userId,
                                                                 @RequestParam("eventId") @Positive Long eventId) {
        ParticipationRequestDto requestDto = userService.createRequest(userId, eventId);
        return new ResponseEntity<>(requestDto, HttpStatus.CREATED);
    }

    @PatchMapping("{userId}" + REQUEST_PATH + "/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long requestId) {
        ParticipationRequestDto requestDto = userService.cancelRequest(userId, requestId);
        return new ResponseEntity<>(requestDto, HttpStatus.OK);
    }

    @GetMapping("{userId}" + EVENTS_PATH + "/{eventId}" + REQUEST_PATH)
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsByInitiator(@PathVariable @Positive Long userId,
                                                                                @PathVariable @Positive Long eventId) {
        List<ParticipationRequestDto> requestDtoList = userService.getRequestsByInitiator(userId, eventId);
        return new ResponseEntity<>(requestDtoList, HttpStatus.OK);
    }

    @PatchMapping("{userId}" + EVENTS_PATH + "/{eventId}" + REQUEST_PATH)
    public ResponseEntity<EventRequestStatusUpdateResult> updateStatusRequest(@PathVariable @Positive Long userId,
                                                                              @PathVariable @Positive Long eventId,
                                                                              @RequestBody(required = false) @Valid EventRequestStatusUpdateRequest request) {
        EventRequestStatusUpdateResult result = userService.updateStatusRequest(userId, eventId, request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
