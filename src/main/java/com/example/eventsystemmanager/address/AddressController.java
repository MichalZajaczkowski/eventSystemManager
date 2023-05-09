package com.example.eventsystemmanager.address;

import com.example.eventsystemmanager.exception.AddressCreationException;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    private final AddressService addressService;

    @GetMapping()
    public ResponseEntity<List<AddressDto>> getUsersAddress() {
        List<AddressDto> addressDtoList = addressService.findAll();
        if (addressDtoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            log.info("Address returned: " + addressDtoList.stream().toList());
            return ResponseEntity.ok(addressDtoList);
        }
    }

    @Operation(summary = "Gets userAddress by id", description = "Get userAddress based on it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(schema = @Schema(implementation = AddressDto.class))),
            @ApiResponse(responseCode = "404", description = "userAddress cannot be found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getUsersAddressById(@Parameter(description = "userAddress to be saved") @PathVariable Long id) {
        AddressDto addressDto = addressService.findById(id);
        log.info("Address found: " + addressDto.toString());
        return addressDto != null ? new ResponseEntity<>(addressDto, HttpStatus.OK) : ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<AddressDto> save(@Valid @RequestBody AddressDto addressDto) {
        try {
            addressService.createAddress(addressDto);
            log.info("Address was created");
            return new ResponseEntity<>(addressDto, HttpStatus.CREATED);
        } catch (AddressCreationException ex) {
            log.error("Address could not be created: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @PatchMapping("/{addressId}/address")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<Map<String, Object>> partialUpdateUserAddress(@PathVariable Long addressId, @RequestBody AddressDto addressDto) {
        Map<String, Object> changedFields = addressService.partialUpdateAddress(addressId, addressDto);
        log.info("Address was updated to " + changedFields);
        return new ResponseEntity<>(changedFields, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAddress(@PathVariable Long id) {
        addressService.removeAddress(id);
        log.info("log:Address removed successfully.");
        return ResponseEntity.noContent().build();

    }
}
