package ru.practicum.huuudrich.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.huuudrich.model.event.*;

import java.util.List;

@Mapper
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "category", ignore = true)
    NewEventDto eventToNewEventDto(Event event);

    @Mapping(target = "category", ignore = true)
    Event newEventDtoToEvent(NewEventDto newEventDto);

    EventShortDto toShortDto(Event entity);

    List<EventShortDto> toShortDtoList(List<Event> events);

    EventFullDto toFullDto(Event entity);

    @Mapping(target = "category", ignore = true)
    Event updateEventToEventAdmin(UpdateEventAdminRequest updateEventRequest);

    List<EventFullDto> toListFullDto(List<Event> events);

    @Mapping(target = "category", ignore = true)
    Event updateEventToEventRequest(UpdateEventUserRequest updateEventUserRequest);

    @Mapping(target = "category", ignore = true)
    UpdateEventUserRequest toUpdateEvent(Event event);
}
