package com.example.eventsystemmanager.place;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaceMapper {

    PlaceDto placeMapToDto(PlaceEntity placeEntity);

    PlaceEntity placeMapToEntity(PlaceDto placeDto);

    PlaceEntity placeMapToEntity(PlaceEntity place);
}
