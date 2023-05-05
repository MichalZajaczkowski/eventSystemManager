package com.example.eventsystemmanager.AdditionalEndpoints;

import com.example.eventsystemmanager.event.EventDto;
import com.example.eventsystemmanager.event.EventEntity;
import com.example.eventsystemmanager.event.EventMapper;
import com.example.eventsystemmanager.exception.EventNotFoundException;
import com.example.eventsystemmanager.exception.OrganizerNotFoundException;
import com.example.eventsystemmanager.organizer.OrganizerEntity;
import com.example.eventsystemmanager.organizer.OrganizerRepository;
import com.example.eventsystemmanager.place.PlaceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionalService {

    private final AdditionalRepository additionalRepository;
    private final OrganizerRepository organizerRepository;
    private final EventMapper eventMapper;
    private final PlaceMapper placeMapper;
    public AdditionalService(AdditionalRepository additionalRepository, OrganizerRepository organizerRepository, EventMapper eventMapper, PlaceMapper placeMapper) {
        this.additionalRepository = additionalRepository;
        this.organizerRepository = organizerRepository;
        this.eventMapper = eventMapper;
        this.placeMapper = placeMapper;
    }

    public List<EventDto> searchEventsByOrganizerNameOrPlaceName(String organizerName, String placeName) throws EventNotFoundException {
        List<EventEntity> events = additionalRepository.findByOrganizerOrPlace(organizerName, placeName);
        if (events.isEmpty()) {
            throw new EventNotFoundException("No events found with given search parameters");
        }
        return eventMapper.toDtoList(events);
    }

    public List<String> searchPlacesByOrganizerName(String organizerName) throws OrganizerNotFoundException {
        List<String> places = additionalRepository.findPlacesByOrganizerName(organizerName);
        if (places.isEmpty()) {
            throw new OrganizerNotFoundException("No places found for the given organizer name");
        }
        return places;
    }

    public Integer getEventsCountByOrganizerName(String name) throws OrganizerNotFoundException {
        OrganizerEntity organizer = organizerRepository.findByName(name)
                .orElseThrow(() -> new OrganizerNotFoundException("Organizer with name " + name + " not found"));
        return additionalRepository.countByOrganizer(organizer);
    }
}

