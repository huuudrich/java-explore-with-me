package ru.practicum.huuudrich.service.subscriptions;

import ru.practicum.exception.BadRequestException;
import ru.practicum.huuudrich.model.event.EventShortDto;
import ru.practicum.huuudrich.model.subscription.UserSubscribeDto;

import java.util.List;

public interface SubscriptionsService {

    UserSubscribeDto addSubscribe(Long followedId, Long followerId) throws BadRequestException;

    UserSubscribeDto getSubscription(Long userId);

    void removeSubscribe(Long userId, Long subscribedUserId);

    List<EventShortDto> getSubscribedAuthorEvents(Long userId, Long initiatorId);

    List<EventShortDto> getSubscribedUsersEvents(Long userId);
}
