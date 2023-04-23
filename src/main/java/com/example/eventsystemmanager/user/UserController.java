package com.example.eventsystemmanager.user;

import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.AddressMapper;
import com.example.eventsystemmanager.user.userStatus.UserStatus;
import com.example.eventsystemmanager.user.userStatus.UserStatusDto;
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
    private final AddressMapper addressMapper;

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
    @GetMapping("/status/name/{statusName}")
    public ResponseEntity<List<UserDto>> getUsersByStatusName(@PathVariable String statusName) {
        List<UserDto> user = userService.getUsersByStatusName(statusName);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return new ResponseEntity<>( user, HttpStatus.OK);
        }
    }
    @GetMapping("/status/value/{statusValue}")
    public ResponseEntity<List<UserDto>> getUsersByStatusValue(@PathVariable Integer statusValue) {
        List<UserDto> user = userService.getUsersByStatusValue(statusValue);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return new ResponseEntity<>( user, HttpStatus.OK);
        }
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
    @PutMapping("/{userId}/address")
    public ResponseEntity<AddressDto> updateUserAddress(@PathVariable Long userId, @RequestBody AddressDto addressDto) {
        AddressEntity updatedAddress = userService.updateAddressForUser(userId, addressDto);
        return new ResponseEntity<>(addressMapper.addressMapToDto(updatedAddress), HttpStatus.OK);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @PutMapping()
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation(value = "Changes user status", notes = "Changes status of user with specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User status changed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<UserDto> changeUserStatus(@PathVariable Long id, @RequestBody UserStatusDto userStatusDto) {
        UserDto user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserStatus userStatus = UserStatus.valueOf(String.valueOf(userStatusDto.getUserStatus()));
        userService.setStatus(user, userStatus);
        log.info("User status changed successfully.");
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        userService.removeUser(id);
        log.info("log: User removed successfully.");
        return ResponseEntity.noContent().build();
    }
}