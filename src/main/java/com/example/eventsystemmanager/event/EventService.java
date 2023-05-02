package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.address.AddressMapper;
import com.example.eventsystemmanager.address.AddressRepository;
import com.example.eventsystemmanager.category.CategoryRepository;
import com.example.eventsystemmanager.category.CategoryService;
import com.example.eventsystemmanager.event.eventStatus.EventStatus;
import com.example.eventsystemmanager.organizer.*;
import com.example.eventsystemmanager.place.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final OrganizerRepository organizerRepository;
    private final PlaceRepository placeRepository;
    private final OrganizerService organizerService;
    private final PlaceService placeService;
    private final EventMapper eventMapper;
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
    public EventDto createEvent(EventDto eventDto) {
        EventEntity eventEntity = eventMapper.toEntity(eventDto);
        try {
            if (eventDto.getOrganizer() != null) {
                OrganizerEntity organizerEntity = organizerRepository.findOrgByName(eventDto.getOrganizer().getName());
                if (organizerEntity == null) {
                    OrganizerDto organizerDto = organizerService.createOrganizer(eventDto.getOrganizer());
                    organizerEntity = organizerRepository.findOrgByName(eventDto.getOrganizer().getName());
                }
                eventEntity.setOrganizer(organizerEntity);
            }
        } catch (Exception e) {
            System.out.println("Error while creating organizer: " + e.getMessage());
            return null;
        }

        try {
            if (eventDto.getPlace() != null) {
                PlaceEntity placeEntity = placeRepository.findByName(eventDto.getPlace().getName());
                if (placeEntity == null) {
                    PlaceDto placeDto = placeService.createPlace(eventDto.getPlace());
                    placeEntity = placeRepository.findByName(eventDto.getPlace().getName());
                }
                eventEntity.setPlace(placeEntity);
            }
        } catch (Exception e) {
            System.out.println("Error while creating place: " + e.getMessage());
            return null;
        }
        eventEntity.setEventStatus(EventStatus.UPCOMING);
        eventEntity.setCreateDate(LocalDateTime.now());
        eventEntity.setModifyDate(LocalDateTime.now());

        try {
            EventEntity savedEvent = eventRepository.save(eventEntity);
            return eventMapper.toDto(savedEvent);
        } catch (Exception e) {
            System.out.println("Error while saving event: " + e.getMessage());
            return null;
        }
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
        eventDto.setCategory(eventEntity.getCategory());
        eventDto.setEventStatus(eventEntity.getEventStatus());
        return eventDto;
    }
}
