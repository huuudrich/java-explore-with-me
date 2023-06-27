package ru.practicum.huuudrich.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.huuudrich.model.user.User;
import ru.practicum.huuudrich.model.user.UserDto;
import ru.practicum.huuudrich.model.user.UserShortDto;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserShortDto initiatorToUserShortDto(User user);

    List<UserShortDto> toListUserShort(List<User> users);

    User toEntity(UserDto dto);

    UserDto toDto(User user);

    List<UserDto> toListDto(List<User> users);
}
