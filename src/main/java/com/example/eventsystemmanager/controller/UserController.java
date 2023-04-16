package com.example.eventsystemmanager.controller;

import com.example.eventsystemmanager.dto.StatusDto;
import com.example.eventsystemmanager.dto.UserDto;
import com.example.eventsystemmanager.enums.StatusType;
import com.example.eventsystemmanager.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/users")
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
    @PostMapping("/create")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        UserDto savedUserDto = userService.createUser(userDto);
        log.info("Log: User was created");
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        //return ResponseEntity.created(URI.create("/api/v1/users/" + savedUserDto.getId())).body(savedUserDto);
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

    @ApiOperation(value = "Changes user status", notes = "Changes status of user with specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User status changed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<UserDto> changeUserStatus(@PathVariable Long id, @RequestBody StatusDto statusDto) {
        UserDto user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        StatusType statusType = StatusType.valueOf(String.valueOf(statusDto.getStatusType()));
        userService.setStatus(user, statusType);
        log.info("User status changed successfully.");
        return ResponseEntity.ok(user);
    }

    @GetMapping("/status/name/{statusName}")
    public List<UserDto> getUsersByStatusName(@PathVariable String statusName) {
        return userService.getUsersByStatusName(statusName);
    }


    @GetMapping("/status/value/{statusValue}")
    public List<UserDto> getUsersByStatusValue(@PathVariable Integer statusValue) {
        return userService.getUsersByStatusValue(statusValue);
    }


}