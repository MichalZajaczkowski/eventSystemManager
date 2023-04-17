package com.example.eventsystemmanager.controller;

import com.example.eventsystemmanager.dto.UserStatusDto;
import com.example.eventsystemmanager.enums.UserStatus;
import com.example.eventsystemmanager.service.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public ResponseEntity<List<UserStatusDto>> getAllStatuses() {
        List<UserStatusDto> userStatusDtos = statusService.getAllStatuses();
        return ResponseEntity.ok(userStatusDtos);
    }

    @GetMapping("/{value}")
    public ResponseEntity<Integer> getStatusValue(@PathVariable Integer value) {
        try {
            UserStatus userStatus = UserStatus.fromValue(value);
            return ResponseEntity.ok(userStatus.getValue());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name")
    public ResponseEntity<String> getStatusName(@PathVariable String name) {
        try {
            UserStatus userStatus = UserStatus.fromName(name);
            return ResponseEntity.ok(userStatus.getName());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

