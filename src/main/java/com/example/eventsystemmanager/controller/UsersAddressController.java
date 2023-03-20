package com.example.eventsystemmanager.controller;

import com.example.eventsystemmanager.dto.UserAddressDto;
import com.example.eventsystemmanager.entity.UserAddress;
import com.example.eventsystemmanager.repository.UserAddressRepository;
import com.example.eventsystemmanager.service.UserAddressService;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/userAddress")
public class UsersAddressController {
    private final UserAddressRepository userAddressRepository;

    private final UserAddressService userAddressService;

    @GetMapping()
    public ResponseEntity<List<UserAddressDto>> getUsersAddress() {
        List<UserAddressDto> usersAddressDtoList = userAddressService.findAll();
        if (usersAddressDtoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            log.info("UsersAddress returned: " + userAddressRepository.findAll());
            return ResponseEntity.ok(usersAddressDtoList);
        }
    }

    @Operation(summary = "Gets movie by id", description = "Get movie based on it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(schema = @Schema(implementation = UserAddressDto.class))),
            @ApiResponse(responseCode = "404", description = "Movie cannot be found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserAddressDto> getUsersAddressById(@Parameter(description = "Movie to be saved") @PathVariable Long id) {
        UserAddressDto userAddressDto = userAddressService.findById(id);
        log.info("User address found: " + userAddressRepository.getById(id));
        return userAddressDto != null ? new ResponseEntity<>(HttpStatus.OK) : ResponseEntity.notFound().build();
    }
//    @GetMapping("/{id}")
//    public ResponseEntity getPublisher(@PathVariable Long id) {
//        UserAddressDto userAddressDto = userAddressService.findById(id);
//        userAddressRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User address witch id" + userAddressDto.getId() + " not found"));
//        log.info("Found userAddress: " + userAddressRepository.getById(id));
//        return ResponseEntity.ok("Found userAddress: " + userAddressRepository.getById(id));
//    }

    @PostMapping()
    public ResponseEntity<UserAddressDto> save(@RequestBody UserAddressDto userAddressDto) {
        userAddressService.createUserAddress(userAddressDto);
        log.info("User address was created");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserAddressDto> update(@RequestBody UserAddressDto userAddressDto) {
        userAddressService.updateUserAddress(userAddressDto);
        log.info("User address was updated");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping
    public ResponseEntity<UserAddressDto> partialUpdateUserAddress(@RequestBody UserAddressDto userAddressDto) {
        userAddressService.partialUpdateUserAddress(userAddressDto);
        log.info("User address was updated");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAddress(@PathVariable Long id) {
        userAddressService.removeAddress(id);
        log.info("log:Address removed successfully.");
        return ResponseEntity.noContent().build();

    }
}
