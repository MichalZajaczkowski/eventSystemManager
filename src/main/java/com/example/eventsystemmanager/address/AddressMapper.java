package com.example.eventsystemmanager.address;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto addressMapToDto(AddressEntity addressEntity);
    AddressEntity addressMapToEntity(AddressDto addressDto);
}
