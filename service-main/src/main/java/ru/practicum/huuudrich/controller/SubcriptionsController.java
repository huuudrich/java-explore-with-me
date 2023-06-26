package ru.practicum.huuudrich.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.BadRequestException;
import ru.practicum.huuudrich.model.event.EventShortDto;
import ru.practicum.huuudrich.model.subscription.UserSubscribeDto;
import ru.practicum.huuudrich.model.user.UserShortDto;
import ru.practicum.huuudrich.service.subscriptions.SubscriptionsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/subscribes")
public class SubcriptionsController {

    private final SubscriptionsService subscriptionsService;
    private static final String userId = "{userId}";

    @PostMapping(userId + "/follow/{authorId}")
    public ResponseEntity<UserSubscribeDto> addSubscribe(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long authorId) throws BadRequestException {
        UserSubscribeDto userSubscribeDto = subscriptionsService.addSubscribe(authorId, userId);
        return new ResponseEntity<>(userSubscribeDto, HttpStatus.CREATED);
    }

    @GetMapping(userId)
    public ResponseEntity<UserSubscribeDto> getSubscription(@PathVariable @Positive Long userId) {
        UserSubscribeDto userSubscribeDto = subscriptionsService.getSubscription(userId);
        return new ResponseEntity<>(userSubscribeDto, HttpStatus.OK);
    }

    @DeleteMapping(userId + "/remove/{subscribedUserId}")
    public ResponseEntity<Void> removeSubscribe(@PathVariable @Positive Long userId,
                                                @PathVariable @Positive Long subscribedUserId) {
        subscriptionsService.removeSubscribe(userId, subscribedUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(userId + "/events/{initiatorId}")
    public ResponseEntity<List<EventShortDto>> getSubscribedAuthorEvents(@PathVariable @Positive Long userId,
                                                                         @PathVariable @Positive Long initiatorId) {
        List<EventShortDto> eventShortDtoList = subscriptionsService.getSubscribedAuthorEvents(userId, initiatorId);
        return new ResponseEntity<>(eventShortDtoList, HttpStatus.OK);
    }

    @PatchMapping(userId)
    public ResponseEntity<UserSubscribeDto> updateAllowSubscription(@PathVariable @Positive Long userId,
                                                                    @RequestParam Boolean status) {
        UserSubscribeDto userSubscribeDto = subscriptionsService.updateAllowSubscription(userId, status);
        return new ResponseEntity<>(userSubscribeDto, HttpStatus.OK);
    }

    @GetMapping(userId + "/followers")
    public ResponseEntity<List<UserShortDto>> getFollowers(@PathVariable @Positive Long userId,
                                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0", required = false) Integer from,
                                                           @PositiveOrZero @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "id"));
        List<UserShortDto> userFollowers = subscriptionsService.getFollowers(userId, pageable);
        return new ResponseEntity<>(userFollowers, HttpStatus.OK);
    }
}
