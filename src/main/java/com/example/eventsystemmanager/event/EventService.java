package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    public List<EventDto> findAll() {
        return eventRepository.findAll()
                .stream()
                .map(this::mapEventToDto)
                .toList();
    }

    public EventDto findById(Long id) {
        return eventRepository.findById(id)
                .map(this::mapEventToDto)
                .orElseThrow(() -> new IllegalArgumentException("komunikat do napisania "));
    }

    private EventDto mapEventToDto(EventEntity eventEntity) {
        EventDto eventDto = new EventDto();
        eventDto.setId(eventEntity.getId());
        eventDto.setName(eventEntity.getName());
        eventDto.setDescription(eventEntity.getDescription());
        eventDto.setEventStartDate(eventEntity.getEventStartDate());
        eventDto.setEventEndDate(eventEntity.getEventEndDate());
        eventDto.setCreateDate(eventEntity.getCreateDate());
        eventDto.setModifyDate(eventEntity.getModifyDate());
        eventDto.setPlaceToDto(eventEntity.getPlace());
        eventDto.setOrganizerToDto(eventEntity.getOrganizer());
        eventDto.setCategoryToDto(eventEntity.getCategory());
        eventDto.setEventStatus(eventEntity.getEventStatus());
        return eventDto;
    }
}
