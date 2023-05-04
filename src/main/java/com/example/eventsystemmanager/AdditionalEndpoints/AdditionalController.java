package com.example.eventsystemmanager.AdditionalEndpoints;

import com.example.eventsystemmanager.event.EventDto;
import com.example.eventsystemmanager.exception.EventNotFoundException;
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

//    public SearchController(SearchService searchService) {
//        this.searchService = searchService;
//    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDto>> searchEventsByOrganizerNameOrPlaceName(@RequestParam(required = false) String organizerName,
                                                                                 @RequestParam(required = false) String placeName) throws EventNotFoundException {
        List<EventDto> events = additionalService.searchEventsByOrganizerNameOrPlaceName(organizerName, placeName);
        return ResponseEntity.ok(events);
    }
}