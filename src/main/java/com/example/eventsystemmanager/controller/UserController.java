package com.example.eventsystemmanager.controller;

import com.example.eventsystemmanager.dto.UserAddressDto;
import com.example.eventsystemmanager.dto.UserDto;
import com.example.eventsystemmanager.service.UserAddressService;
import com.example.eventsystemmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Address;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Gets all user", description = "Gets list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(schema = @Schema(implementation = UserDto[].class))),
            @ApiResponse(responseCode = "500", description = "Users cannot be found")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getMovies() {
        List<UserDto> user = userService.findAll();
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        log.info("User found: " + userService.findById(id));
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Gets user by id", description = "Get user based on it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User cannot be created"),
            @ApiResponse(responseCode = "500", description = "User cannot be created")
    })
    @PostMapping()
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        log.info("Log: User was created");
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }



    @PatchMapping()
    public ResponseEntity<UserDto> partialUpdateUser(@RequestBody UserDto userDto) {
        userService.partialUpdateUser(userDto);
        log.info("Log: User was updated");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping()
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        userService.removeUser(id);
        log.info("log: User removed successfully.");
        return ResponseEntity.noContent().build();
    }
}
