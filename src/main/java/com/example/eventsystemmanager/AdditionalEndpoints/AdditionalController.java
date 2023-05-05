package com.example.eventsystemmanager.AdditionalEndpoints;

import com.example.eventsystemmanager.event.EventDto;
import com.example.eventsystemmanager.exception.EventNotFoundException;
import com.example.eventsystemmanager.exception.OrganizerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/")
public class AdditionalController {

    private final AdditionalService additionalService;

    @GetMapping("/search")
    public ResponseEntity<List<EventDto>> searchEventsByOrganizerNameOrPlaceName(@RequestParam(required = false) String organizerName,
                                                                                 @RequestParam(required = false) String placeName) throws EventNotFoundException {
        List<EventDto> events = additionalService.searchEventsByOrganizerNameOrPlaceName(organizerName, placeName);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/organizer-places")
    public ResponseEntity<List<String>> searchPlacesByOrganizerName(@RequestParam String organizerName) throws OrganizerNotFoundException {
        List<String> places = additionalService.searchPlacesByOrganizerName(organizerName);
        return ResponseEntity.ok(places);
    }

    @GetMapping("/organizers/{name}/events-count")
    public ResponseEntity<Integer> getEventsCountByOrganizerName(@PathVariable String name) throws OrganizerNotFoundException {
        Integer eventsCount = additionalService.getEventsCountByOrganizerName(name);
        return ResponseEntity.ok(eventsCount);
    }
}