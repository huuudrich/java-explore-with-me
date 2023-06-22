package ru.practicum.huuudrich.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.huuudrich.model.request.ParticipationRequestDto;
import ru.practicum.huuudrich.model.request.Request;

import java.util.List;

@Mapper
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "event", ignore = true)
    @Mapping(target = "requester", ignore = true)
    Request requestDtoToRequest(ParticipationRequestDto participationRequestDto);

    @Mapping(target = "event", ignore = true)
    @Mapping(target = "requester", ignore = true)
    ParticipationRequestDto toRequestDto(Request request);

    @Mapping(target = "event", ignore = true)
    @Mapping(target = "requester", ignore = true)
    List<Request> listRequestDtoToRequest(List<ParticipationRequestDto> requestDtoList);

    @Mapping(target = "event", ignore = true)
    @Mapping(target = "requester", ignore = true)
    List<ParticipationRequestDto> listToRequestDto(List<Request> requests);
}
