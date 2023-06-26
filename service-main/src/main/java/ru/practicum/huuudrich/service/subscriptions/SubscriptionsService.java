package ru.practicum.huuudrich.service.subscriptions;

import org.springframework.data.domain.Pageable;
import ru.practicum.exception.BadRequestException;
import ru.practicum.huuudrich.model.event.EventShortDto;
import ru.practicum.huuudrich.model.subscription.UserSubscribeDto;
import ru.practicum.huuudrich.model.user.UserShortDto;

import java.util.List;

public interface SubscriptionsService {

    UserSubscribeDto addSubscribe(Long followedId, Long followerId) throws BadRequestException;

    UserSubscribeDto getSubscription(Long userId);

    UserSubscribeDto updateAllowSubscription(Long userId, Boolean status);

    void removeSubscribe(Long userId, Long subscribedUserId);

    List<EventShortDto> getSubscribedAuthorEvents(Long userId, Long initiatorId);

    List<UserShortDto> getFollowers(Long userId, Pageable pageable);

    List<EventShortDto> getSubscribedUsersEvents(Long userId);
}
