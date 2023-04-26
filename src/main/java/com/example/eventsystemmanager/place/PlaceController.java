package com.example.eventsystemmanager.place;

import com.example.eventsystemmanager.address.AddressMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
