package com.example.eventsystemmanager.controller;

import com.example.eventsystemmanager.dto.UserAddressDto;
import com.example.eventsystemmanager.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/userAddress")
public class UsersAddressController {

    private final UserAddressService userAddressService;

    @GetMapping()
    public ResponseEntity<List<UserAddressDto>> getUsersAddress() {
        List<UserAddressDto> usersAddressDtoList = userAddressService.findAll();
        if (usersAddressDtoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(usersAddressDtoList);
        }
    }

    @PostMapping()
    public ResponseEntity<UserAddressDto> save(@RequestBody UserAddressDto userAddressDto) {
        userAddressService.createUserAddress(userAddressDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

