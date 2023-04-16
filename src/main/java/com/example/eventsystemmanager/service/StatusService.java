package com.example.eventsystemmanager.service;

import com.example.eventsystemmanager.dto.StatusDto;
import com.example.eventsystemmanager.entity.StatusEntity;
import com.example.eventsystemmanager.enums.StatusType;
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

    public Integer getStatusValue(Integer value) {
        try {
            StatusType statusType = StatusType.fromValue(value);
            return statusType.getValue();
        } catch (IllegalArgumentException e) {
            return -1; // wartość nieznana
        }
    }

    public String getStatusName(String name) {
        try {
            StatusType statusType = StatusType.fromName(name);
            return statusType.getName();
        } catch (IllegalArgumentException e) {
            return "Unknown"; // wartość nieznana
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

    public void updateStatus(String name, StatusDto newStatusDto) {
        StatusType statusType = StatusType.fromName(name);
        StatusEntity statusEntity = statusRepository.findByValue(statusType.getValue());
        if (statusEntity == null) {
            throw new IllegalArgumentException("Nieprawidłowa nazwa statusu: " + name);
        }
        statusEntity.setName(newStatusDto.getName());
        statusEntity.setDescription(newStatusDto.getDescription());
        statusRepository.save(statusEntity);
    }
}