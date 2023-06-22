package ru.practicum.huuudrich.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.huuudrich.model.user.User;
import ru.practicum.huuudrich.model.user.UserDto;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserDto dto);

    UserDto toDto(User user);

    List<UserDto> toListDto(List<User> users);
}
