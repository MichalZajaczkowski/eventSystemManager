package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.organizer.OrganizerDto;
import com.example.eventsystemmanager.place.PlaceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@Slf4j
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventDto) {
        EventDto createdEvent = eventService.createEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PatchMapping("/{eventId}/modifyEvent")
    public ResponseEntity<EventDto> modifyEvent(@PathVariable Long eventId, @RequestBody EventDto eventDto) {
        eventDto.setId(eventId);
        EventDto updatedEvent = eventService.partialUpdateEventsData(eventId, eventDto);
        log.debug("Log: Event was updated");
        return new ResponseEntity<>(updatedEvent, HttpStatus.ACCEPTED);
    }
    @PatchMapping("/{eventId}/updatePlace")
    public ResponseEntity<EventDto> updatePlace(@PathVariable Long eventId, @RequestBody PlaceDto placeId) {
        EventDto updatedEvent = eventService.updatePlaceForEvent(eventId, placeId);
        return ResponseEntity.ok(updatedEvent);
    }



    @PatchMapping("/{eventId}/updateOrganizer")
    public ResponseEntity<EventDto> updateOrganizer(@PathVariable Long eventId, @RequestBody OrganizerDto organizerId) {
        EventDto updatedEvent = eventService.updateOrganizerForEvent(eventId, organizerId);
        return ResponseEntity.ok(updatedEvent);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<EventEntity> getEventById(@PathVariable Long id) {
//        try {
//            EventEntity event = eventService.getEventById(id);
//            return ResponseEntity.ok(event);
//        } catch (EventNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<List<EventEntity>> getAllEvents() {
//        List<EventEntity> events = eventService.getAllEvents();
//        return ResponseEntity.ok(events);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteEventById(@PathVariable Long id) {
//        try {
//            eventService.deleteEventById(id);
//            return ResponseEntity.noContent().build();
//        } catch (EventNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<EventEntity> updateEvent(@PathVariable Long id, @RequestBody UpdateEventRequest request) {
//        try {
//            EventEntity updatedEvent = eventService.updateEvent(id, request);
//            return ResponseEntity.ok(updatedEvent);
//        } catch (EventNotFoundException | PlaceNotFoundException | AddressNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}

