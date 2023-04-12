package com.example.eventsystemmanager.service;

import com.example.eventsystemmanager.dto.StatusDto;
import com.example.eventsystemmanager.entity.StatusEntity;
import com.example.eventsystemmanager.enums.UserStatus;
import com.example.eventsystemmanager.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Integer getStatusValue(String name) {
        try {
            UserStatus userStatus = UserStatus.fromName(name);
            return userStatus.getValue();
        } catch (IllegalArgumentException e) {
            return -1; // wartość nieznana
        }
    }

    public List<StatusDto> getAllStatuses() {
        List<StatusEntity> statuses = statusRepository.findAll();

        return statuses.stream().map(status -> {
            StatusDto statusDto = new StatusDto();
            statusDto.setName(status.getName());
            statusDto.setDescription(status.getDescription());
            return statusDto;
        }).collect(Collectors.toList());
    }
}
