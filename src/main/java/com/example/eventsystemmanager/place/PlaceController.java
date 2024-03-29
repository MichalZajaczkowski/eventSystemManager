package com.example.eventsystemmanager.place;

import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.AddressMapper;
import com.example.eventsystemmanager.exception.DuplicateEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/place")
public class PlaceController {

    private final PlaceService placeService;
    private final AddressMapper addressMapper;

    @PostMapping("/create")
    public ResponseEntity<?> save(@Valid @RequestBody PlaceDto placeDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessages(result));
        }
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
    public ResponseEntity<?> updatePlaceData(@PathVariable Long placeId, @Valid @RequestBody PlaceDto placeDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessages(result));
        }
        placeService.updatePlaceData(placeId, placeDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{placeId}/partialUpdatePlaceData")
    public ResponseEntity<Map<String, Object>> partialUpdatePlaceData(@PathVariable Long placeId, @Valid @RequestBody PlaceDto placeDto) {
        Map<String, Object> changedFields = placeService.partialUpdatePlaceData(placeId, placeDto);
        log.debug("Log: Place was updated");
        return new ResponseEntity<>(changedFields, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{placeId}/updateAddressForPlace")
    public ResponseEntity<AddressDto> updateAddressForPlace(@PathVariable Long placeId, @RequestBody AddressDto addressDto) {
        AddressEntity updatedAddress = placeService.updateAddressForPlace(placeId, addressDto);
        return new ResponseEntity<>(addressMapper.addressMapToDto(updatedAddress), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        placeService.removePlace(id);
        log.debug("log: Place removed successfully.");
        return ResponseEntity.noContent().build();
    }

    private List<String> getErrorMessages(BindingResult result) {
        List<String> errorMessages = new ArrayList<>();
        for (FieldError error : result.getFieldErrors()) {
            errorMessages.add(error.getDefaultMessage());
        }
        return errorMessages;
    }

}
