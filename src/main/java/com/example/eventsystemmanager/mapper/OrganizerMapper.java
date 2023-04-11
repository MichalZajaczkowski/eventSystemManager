package com.example.eventsystemmanager.mapper;

import com.example.eventsystemmanager.dto.OrganizerDto;
import com.example.eventsystemmanager.entity.OrganizerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizerMapper {
    OrganizerDto organizerMapToDto(OrganizerEntity organizerEntity);
}
