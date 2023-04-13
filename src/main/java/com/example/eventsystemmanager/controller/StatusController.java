package com.example.eventsystemmanager.controller;

import com.example.eventsystemmanager.dto.StatusDto;
import com.example.eventsystemmanager.enums.StatusType;
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
    public ResponseEntity<List<StatusDto>> getAllStatuses() {
        List<StatusDto> statusDtos = statusService.getAllStatuses();
        return ResponseEntity.ok(statusDtos);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Integer> getStatusValue(@PathVariable String name) {
        try {
            StatusType statusType = StatusType.fromName(name);
            return ResponseEntity.ok(statusType.getValue());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

