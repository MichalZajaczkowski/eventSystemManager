package com.example.eventsystemmanager.status;

import com.example.eventsystemmanager.user.userStatus.UserStatusDto;
import com.example.eventsystemmanager.user.userStatus.UserStatusEntity;
import com.example.eventsystemmanager.user.userStatus.UserStatus;
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
            UserStatus userStatus = UserStatus.fromValue(value);
            return userStatus.getValue();
        } catch (IllegalArgumentException e) {
            return -1; // wartość nieznana
        }
    }

    public String getStatusName(String name) {
        try {
            UserStatus userStatus = UserStatus.fromName(name);
            return userStatus.getName();
        } catch (IllegalArgumentException e) {
            return "Unknown"; // wartość nieznana
        }
    }

    public List<UserStatusDto> getAllStatuses() {
        List<UserStatusEntity> statuses = statusRepository.findAll();

        return statuses.stream().map(status -> {
            UserStatusDto userStatusDto = new UserStatusDto();
            userStatusDto.setName(status.getName());
            userStatusDto.setDescription(status.getDescription());
            return userStatusDto;
        }).collect(Collectors.toList());
    }

    public void updateStatus(String name, UserStatusDto newUserStatusDto) {
        UserStatus userStatus = UserStatus.fromName(name);
        UserStatusEntity userStatusEntity = statusRepository.findByValue(userStatus.getValue());
        if (userStatusEntity == null) {
            throw new IllegalArgumentException("Nieprawidłowa nazwa statusu: " + name);
        }
        userStatusEntity.setName(newUserStatusDto.getName());
        userStatusEntity.setDescription(newUserStatusDto.getDescription());
        statusRepository.save(userStatusEntity);
    }
}
