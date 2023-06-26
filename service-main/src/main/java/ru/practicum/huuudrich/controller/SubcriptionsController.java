package ru.practicum.huuudrich.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.BadRequestException;
import ru.practicum.huuudrich.model.event.EventShortDto;
import ru.practicum.huuudrich.model.subscription.UserSubscribeDto;
import ru.practicum.huuudrich.service.subscriptions.SubscriptionsService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/subscribes")
public class SubcriptionsController {

    private final SubscriptionsService subscriptionsService;

    @PostMapping("{userId}/follow/{authorId}")
    public ResponseEntity<UserSubscribeDto> addSubscribe(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long authorId) throws BadRequestException {
        UserSubscribeDto userSubscribeDto = subscriptionsService.addSubscribe(authorId, userId);
        return new ResponseEntity<>(userSubscribeDto, HttpStatus.CREATED);
    }

    @GetMapping("{userId}/follow")
    public ResponseEntity<UserSubscribeDto> getSubscription(@PathVariable @Positive Long userId) {
        UserSubscribeDto userSubscribeDto = subscriptionsService.getSubscription(userId);
        return new ResponseEntity<>(userSubscribeDto, HttpStatus.OK);
    }

    @DeleteMapping("{userId}/follow/{subscribedUserId}")
    public ResponseEntity<Void> removeSubscribe(@PathVariable @Positive Long userId,
                                                @PathVariable @Positive Long subscribedUserId) {
        subscriptionsService.removeSubscribe(userId, subscribedUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{userId}/events/{initiatorId}")
    public ResponseEntity<List<EventShortDto>> getSubscribedAuthorEvents(@PathVariable @Positive Long userId,
                                                                         @PathVariable @Positive Long initiatorId) {
        List<EventShortDto> eventShortDtoList = subscriptionsService.getSubscribedAuthorEvents(userId, initiatorId);
        return new ResponseEntity<>(eventShortDtoList, HttpStatus.OK);
    }
}
