package com.example.eventsystemmanager.organizer;

import com.example.eventsystemmanager.organizer.OrganizerDto;
import com.example.eventsystemmanager.organizer.OrganizerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizerMapper {
    OrganizerDto organizerMapToDto(OrganizerEntity organizerEntity);
}
