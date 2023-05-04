package com.example.eventsystemmanager.event;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDto toDto(EventEntity eventEntity);

    EventEntity toEntity(EventDto eventDto);

    List<EventDto> toDtoList(List<EventEntity> eventEntities);

    List<EventEntity> toEntityList(List<EventDto> eventDtos);
}