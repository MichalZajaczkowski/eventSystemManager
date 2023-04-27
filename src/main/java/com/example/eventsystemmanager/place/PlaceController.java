package com.example.eventsystemmanager.place;

import com.example.eventsystemmanager.address.AddressMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/place")
public class PlaceController {

    private final PlaceService placeService;
    private final AddressMapper addressMapper;

    @PostMapping("/create")
    public ResponseEntity<PlaceDto> save(@RequestBody PlaceDto placeDto) {
        PlaceDto savedPlaceDto = placeService.createPlace(placeDto);
        log.info("Log: Place was created");
        return new ResponseEntity<>(placeDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlaceDto>> getPlaces() {
        List<PlaceDto> result = placeService.findAll();
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDto> getProduct(@PathVariable Long id) {
        PlaceDto place = placeService.findById(id);
        log.info("Place found: " + placeService.findById(id));
        return place != null ? ResponseEntity.ok(place) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{placeId}/updatePlaceData")
    public ResponseEntity<PlaceDto> updatePlaceData(@PathVariable Long placeId,@RequestBody PlaceDto placeDto) {
        placeService.updatePlaceData(placeId, placeDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{placeId}/partialUpdatePlaceData")
    public ResponseEntity<PlaceDto> partialUpdatePlaceData(@PathVariable Long placeId,@RequestBody PlaceDto placeDto) {
        placeService.partialUpdatePlaceData(placeId, placeDto);
        log.debug("Log: Place was updated");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
