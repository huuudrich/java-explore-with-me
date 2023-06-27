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

    @Mapping(target = "event", source = "request.event.id")
    @Mapping(target = "requester", source = "request.requester.id")
    ParticipationRequestDto toRequestDto(Request request);

    @Mapping(target = "event.id", source = "participationRequestDto.event")
    @Mapping(target = "requester.id", source = "participationRequestDto.requester")
    Request requestDtoToRequest(ParticipationRequestDto participationRequestDto);

    List<Request> listRequestDtoToRequest(List<ParticipationRequestDto> requestDtoList);

    List<ParticipationRequestDto> listToRequestDto(List<Request> requests);
}
