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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Slf4j
@Service
public class EventService {
    private static final String PLACE_WITH_ID_DOES_NOT_EXIST = "Place with id " + id + " does not exist.";
    private static final String ORGANIZER_WITH_ID_DOES_NOT_EXIST = "Organizer with id " + id + " does not exist.";
    private static final String EVENT_WITH_ID_DOES_NOT_EXIST = "Event with id " + id + " does not exist.";
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

    public EventDto partialUpdateEventsData(Long eventId, EventDto eventDto) {
        EventEntity eventToUpdate = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException(EVENT_WITH_ID_DOES_NOT_EXIST));

        if (eventDto.getCategory() != null) {
            eventToUpdate.setCategory(eventDto.getCategory());
        }
        if (eventDto.getName() != null) {
            eventToUpdate.setName(eventDto.getName());
        }
        if (eventDto.getDescription() != null) {
            eventToUpdate.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventStartDate() != null) {
            eventToUpdate.setEventStartDate(eventDto.getEventStartDate());
        }
        if (eventDto.getEventEndDate() != null) {
            eventToUpdate.setEventEndDate(eventDto.getEventEndDate());
        }
        eventToUpdate.setModifyDate(LocalDateTime.now());
        eventRepository.save(eventToUpdate);
        return eventDto;
    }

    public EventDto updatePlaceForEvent(Long eventId, PlaceDto placeId) {
        EventEntity eventToUpdate = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException(EVENT_WITH_ID_DOES_NOT_EXIST));

        // Jeśli miejsce nie zostanie zmienione, to nie wykonujemy żadnych akcji
        if (placeId.equals(eventToUpdate.getPlace().getId())) {
            return eventMapper.toDto(eventToUpdate);
        }
        // Pobierz miejsce, które chcemy przypisać do wydarzenia
        PlaceEntity placeToUpdate = placeRepository.findById(placeId.getId())
                .orElseThrow(() -> new IllegalArgumentException(PLACE_WITH_ID_DOES_NOT_EXIST));
        // Ustaw zmodyfikowaną wartość miejsca w wydarzeniu
        eventToUpdate.setPlace(placeToUpdate);
        eventToUpdate.setModifyDate(LocalDateTime.now()); // ustawiamy pole modifyDate

        EventEntity updatedEvent = eventRepository.save(eventToUpdate);
        return eventMapper.toDto(updatedEvent);
    }
    public EventDto updateOrganizerForEvent(Long eventId, OrganizerDto organizerId) {
        EventEntity eventToUpdate = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException(EVENT_WITH_ID_DOES_NOT_EXIST));
        // Jeśli organizator nie zostanie zmienione, to nie wykonujemy żadnych akcji
        if (organizerId.equals(eventToUpdate.getOrganizer().getId())) {
            return eventMapper.toDto(eventToUpdate);
        }
        // Pobierz organizatora, którego chcemy przypisać do wydarzenia

        OrganizerEntity organizerToUpdate = organizerRepository.findById(organizerId.getId())
                .orElseThrow(() -> new IllegalStateException(ORGANIZER_WITH_ID_DOES_NOT_EXIST));

        // Ustaw zmodyfikowaną wartość organizatora w wydarzeniu
        eventToUpdate.setOrganizer(organizerToUpdate);
        eventToUpdate.setModifyDate(LocalDateTime.now()); // ustawienie pola modifyDate
        EventEntity updatedEvent = eventRepository.save(eventToUpdate);
        return eventMapper.toDto(updatedEvent);
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
