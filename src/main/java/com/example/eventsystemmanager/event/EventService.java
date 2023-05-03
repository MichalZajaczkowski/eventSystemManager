package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.event.eventStatus.EventStatus;
import com.example.eventsystemmanager.exception.EventSaveException;
import com.example.eventsystemmanager.exception.OrganizerNotFoundException;
import com.example.eventsystemmanager.exception.PlaceNotFoundException;
import com.example.eventsystemmanager.organizer.OrganizerDto;
import com.example.eventsystemmanager.organizer.OrganizerEntity;
import com.example.eventsystemmanager.organizer.OrganizerRepository;
import com.example.eventsystemmanager.organizer.OrganizerService;
import com.example.eventsystemmanager.place.PlaceDto;
import com.example.eventsystemmanager.place.PlaceEntity;
import com.example.eventsystemmanager.place.PlaceRepository;
import com.example.eventsystemmanager.place.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
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
    @Transactional
    public EventDto createEvent(@NotNull @Valid EventDto eventDto) {
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
        } catch (OrganizerNotFoundException e) {
            log.error("Organizer not found: {}", e.getMessage());
            throw e;
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
        } catch (PlaceNotFoundException e) {
            log.error("Place not found: {}", e.getMessage());
            throw e;
        }

        eventEntity.setEventStatus(EventStatus.UPCOMING);
        eventEntity.setCreateDate(LocalDateTime.now());

        try {
            EventEntity savedEvent = eventRepository.save(eventEntity);
            return eventMapper.toDto(savedEvent);
        } catch (Exception e) {
            log.error("Error while saving event: {}", e.getMessage());
            throw new EventSaveException("Error while saving event: " + e.getMessage(), e);
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
