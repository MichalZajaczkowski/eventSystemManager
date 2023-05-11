package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.exception.EventNotFoundException;
import com.example.eventsystemmanager.organizer.OrganizerDto;
import com.example.eventsystemmanager.place.PlaceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
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

    @PostMapping("/createEvent")
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

    @PatchMapping("/{eventId}/partialUpdateEventsData")
    public ResponseEntity<EventDto> partialUpdateEventsData(@PathVariable Long eventId, @RequestBody EventDto eventDto) {
        eventDto.setId(eventId);
        EventDto updatedEvent = eventService.partialUpdateEventsData(eventId, eventDto);
        log.debug("Log: Event was updated");
        return new ResponseEntity<>(updatedEvent, HttpStatus.ACCEPTED);
    }
    @PatchMapping("/{eventId}/updatePlaceForEvent")
    public ResponseEntity<EventDto> updatePlaceForEvent(@PathVariable Long eventId, @RequestBody PlaceDto place) {
        EventDto updatedEvent = eventService.updatePlaceForEvent(eventId, place);
        return ResponseEntity.ok(updatedEvent);
    }
    @PatchMapping("/{eventId}/updateOrganizerForEvent")
    public ResponseEntity<EventDto> updateOrganizerForEvent(@PathVariable Long eventId, @RequestBody OrganizerDto organizerId) {
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
    public ResponseEntity<EventDto> findById(@PathVariable Long id) throws EventNotFoundException {
        try {
            EventDto event = eventService.findById(id);
            return ResponseEntity.ok(event);
        } catch (NoSuchElementException e) {
            throw new EventNotFoundException("Event with id " + id + " not found.");
        }
    }

    @PutMapping("/{eventId}/updateSingleEventStatus")
    public ResponseEntity<Void> updateSingleEventStatus(@PathVariable Long eventId) {
        eventService.updateSingleEventStatus(eventId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateAllEventStatus")
    public ResponseEntity<Void> updateAllEventStatus() {
        eventService.updateAllEventStatus();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{eventId}/setStatus")
    public ResponseEntity<Void> setStatus(@PathVariable Long eventId, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        eventService.setStatus(eventId, status);
        return ResponseEntity.noContent().build();
    }
}

