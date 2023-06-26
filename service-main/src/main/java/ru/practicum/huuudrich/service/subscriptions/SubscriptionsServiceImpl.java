package ru.practicum.huuudrich.service.subscriptions;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.huuudrich.mapper.EventMapper;
import ru.practicum.huuudrich.mapper.SubscribesMapper;
import ru.practicum.huuudrich.mapper.UserMapper;
import ru.practicum.huuudrich.model.event.Event;
import ru.practicum.huuudrich.model.event.EventShortDto;
import ru.practicum.huuudrich.model.subscription.UserSubscribeDto;
import ru.practicum.huuudrich.model.subscription.UserSubscription;
import ru.practicum.huuudrich.model.user.User;
import ru.practicum.huuudrich.model.user.UserShortDto;
import ru.practicum.huuudrich.repository.EventRepository;
import ru.practicum.huuudrich.repository.SubscriptionsRepository;
import ru.practicum.huuudrich.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SubscriptionsServiceImpl implements SubscriptionsService {
    private final UserRepository userRepository;
    private final SubscriptionsRepository subscriptionsRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public UserSubscribeDto addSubscribe(Long followedId, Long followerId) throws BadRequestException {
        if (Objects.equals(followedId, followerId)) {
            throw new BadRequestException("FollowedId and followedId cannot be equal");
        }

        if (subscriptionsRepository.findByFollowerIdAndFollowedId(followerId, followedId)
                .isPresent()) {
            throw new BadRequestException("User is already subscribe");
        }

        User followedUser = getUser(followedId);

        if (!followedUser.getAllowSubscriptions()) {
            throw new DataIntegrityViolationException("User banned subscriptions");
        }

        User followerUser = getUser(followerId);

        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setFollowed(followedUser);
        userSubscription.setFollower(followerUser);

        subscriptionsRepository.save(userSubscription);

        UserSubscribeDto userSubscribeDto = SubscribesMapper.INSTANCE.userToDto(followerUser);

        return setListsUserSubscription(userSubscribeDto, followerUser);
    }

    @Override
    public UserSubscribeDto getSubscription(Long userId) {
        User user = getUser(userId);

        UserSubscribeDto userSubscribeDto = SubscribesMapper.INSTANCE.userToDto(user);

        return setListsUserSubscription(userSubscribeDto, user);
    }

    @Override
    public UserSubscribeDto updateAllowSubscription(Long userId, Boolean status) {
        User user = getUser(userId);

        user.setAllowSubscriptions(status);
        userRepository.save(user);

        UserSubscribeDto userSubscribeDto = SubscribesMapper.INSTANCE.userToDto(user);

        return setListsUserSubscription(userSubscribeDto, user);
    }

    @Transactional
    @Override
    public void removeSubscribe(Long userId, Long subscribedUserId) {
        User user = getUser(userId);
        User subscribedUser = getUser(subscribedUserId);
        UserSubscription userSubscription = getUserSubscribed(user, subscribedUser);

        subscriptionsRepository.delete(userSubscription);

        user.getFollowing().remove(userSubscription);
        subscribedUser.getFollowers().remove(userSubscription);

        userRepository.save(user);
        userRepository.save(subscribedUser);
    }

    @Transactional
    @Override
    public List<EventShortDto> getSubscribedAuthorEvents(Long userId, Long initiatorId) {
        User user = getUser(userId);
        User initiator = getUser(initiatorId);

        Optional<UserSubscription> subscription = subscriptionsRepository.findByFollowerAndFollowed(user, initiator);

        if (subscription.isPresent()) {
            List<Event> events = eventRepository.findByInitiator(initiator);
            return EventMapper.INSTANCE.toShortDtoList(events);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<UserShortDto> getFollowers(Long userId, Pageable pageable) {
        User user = getUser(userId);
        List<User> users = userRepository.getFollowersByUser(user, pageable);

        return UserMapper.INSTANCE.toListUserShort(users);
    }

    @Transactional
    @Override
    public List<EventShortDto> getSubscribedUsersEvents(Long userId) {
        User user = getUser(userId);

        List<UserSubscription> subscriptions = user.getFollowing();

        List<Event> events = subscriptions.stream()
                .map(UserSubscription::getFollowed)
                .flatMap(subscribedUser -> eventRepository.findByInitiator(subscribedUser).stream())
                .collect(Collectors.toList());

        return EventMapper.INSTANCE.toShortDtoList(events);
    }

    private UserSubscribeDto setListsUserSubscription(UserSubscribeDto userSubscribeDto, User user) {
        userSubscribeDto.setFollowers(new ArrayList<>(userRepository.getSubcriptionsIdsByUser(user)));
        userSubscribeDto.setFollowing(new ArrayList<>(userRepository.getFollowedIdsByUser(user)));
        return userSubscribeDto;
    }

    private UserSubscription getUserSubscribed(User follower, User followed) {
        return subscriptionsRepository.findByFollowerAndFollowed(follower, followed)
                .orElseThrow(() -> new EntityNotFoundException("Subscribe not found"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User not found with id: %d", userId)));
    }
}
