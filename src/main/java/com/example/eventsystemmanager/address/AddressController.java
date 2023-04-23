package com.example.eventsystemmanager.address;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/userAddress")
public class AddressController {
    private final AddressRepository addressRepository;
    private final AddressService addressService;

    @GetMapping()
    public ResponseEntity<List<AddressDto>> getUsersAddress() {
        List<AddressDto> usersAddressDtoList = addressService.findAll();
        if (usersAddressDtoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            log.info("UsersAddress returned: " + addressService.findAll());
            return ResponseEntity.ok(usersAddressDtoList);
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
        log.info("User address found: " + addressService.findById(id));
        return addressDto != null ? new ResponseEntity<>(HttpStatus.OK) : ResponseEntity.notFound().build();
    }
//    @GetMapping("/{id}")
//    public ResponseEntity getPublisher(@PathVariable Long id) {
//        UserAddressDto userAddressDto = userAddressService.findById(id);
//        userAddressRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User address witch id" + userAddressDto.getId() + " not found"));
//        log.info("Found userAddress: " + userAddressRepository.getById(id));
//        return ResponseEntity.ok("Found userAddress: " + userAddressRepository.getById(id));
//    }

    @PostMapping()
    public ResponseEntity<AddressDto> save(@RequestBody AddressDto addressDto) {
        addressService.createUserAddress(addressDto);
        log.info("User address was created");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<AddressDto> update(@RequestBody AddressDto addressDto) {
        addressService.updateUserAddress(addressDto);
        log.info("User address was updated");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping
    public ResponseEntity<AddressDto> partialUpdateUserAddress(@RequestBody AddressDto addressDto) {
        addressService.partialUpdateUserAddress(addressDto);
        log.info("User address was updated");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAddress(@PathVariable Long id) {
        addressService.removeAddress(id);
        log.info("log:Address removed successfully.");
        return ResponseEntity.noContent().build();

    }
}
