package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.event.eventStatus.EventStatus;
import com.example.eventsystemmanager.exception.EventNotFoundException;
import com.example.eventsystemmanager.organizer.OrganizerDto;
import com.example.eventsystemmanager.place.PlaceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


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

    @PostMapping("/createByOrganizer")
    @Operation(summary = "Create event by organizer", description = "Create a new event by organizer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully",
                    content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Organizer not found"),
            @ApiResponse(responseCode = "500", description = "An error occurred while creating the event")
    })
    public ResponseEntity<EventDto> createEventByOrganizer(@RequestBody @Valid EventDto eventDto,
                                                           @RequestParam(required = false) Long organizerId,
                                                           @RequestParam(required = false) String organizerName) {
        EventDto createdEvent = eventService.createEventByOrganizer(eventDto, organizerId, organizerName);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
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

    @GetMapping
    public ResponseEntity<List<EventDto>> findAllEvents() {
        List<EventDto> events = eventService.findAllEvents();
        if (events.isEmpty()) {
            log.error("Event not found");
            return ResponseEntity.noContent().build();
        } else {
            log.debug("Event found");
            return ResponseEntity.ok(events);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) throws EventNotFoundException {
        try {
            EventDto event = eventService.findById(id);
            return ResponseEntity.ok(event);
        } catch (NoSuchElementException e) {
            throw new EventNotFoundException("Event with id " + id + " not found.");
        }
    }

    @PutMapping("/{eventId}/status")
    public ResponseEntity<Void> updateEventStatus(@PathVariable Long eventId) {
        eventService.updateEventStatus(eventId);
        return ResponseEntity.noContent().build();
    }
}

