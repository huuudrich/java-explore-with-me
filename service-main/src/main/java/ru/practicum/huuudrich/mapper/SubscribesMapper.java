package ru.practicum.huuudrich.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.huuudrich.model.subscription.UserSubscribeDto;
import ru.practicum.huuudrich.model.user.User;

@Mapper
public interface SubscribesMapper {
    SubscribesMapper INSTANCE = Mappers.getMapper(SubscribesMapper.class);

    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "following", ignore = true)
    UserSubscribeDto userToDto(User user);
}
