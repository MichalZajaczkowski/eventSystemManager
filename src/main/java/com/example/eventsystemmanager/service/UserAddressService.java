package com.example.eventsystemmanager.service;

import com.example.eventsystemmanager.dto.UserAddressDto;
import com.example.eventsystemmanager.entity.UserAddress;
import com.example.eventsystemmanager.mapper.UserAddressMapper;
import com.example.eventsystemmanager.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Service
public class UserAddressService {

    @Autowired
    private final UserAddressRepository userAddressRepository;
    private final UserAddressMapper mapper;

    public List<UserAddressDto> findAll() {
        return userAddressRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private UserAddressDto toDto(UserAddress userAddress) {
        return UserAddressDto.builder()
                .id(userAddress.getId())
                .city(userAddress.getCity())
                .street(userAddress.getStreet())
                .buildingNumber(userAddress.getBuildingNumber())
                .localNumber(userAddress.getLocalNumber())
                .postCode(userAddress.getPostCode())
                .build();

    }

    public UserAddressDto findById(Long id) {
        return userAddressRepository.findById(id)
                .map(mapper::userAddressMapToDto)
                .orElseThrow(() -> new IllegalArgumentException("UserAddress with id " + id + " does not exist"));
    }

    public void createUserAddress(UserAddressDto userAddressDto) {
        if (userAddressRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("UserAddress with id " + id + " already exists");
        } else {
            userAddressRepository.save(userAddressDto.toUserAddress());
        }
    }

    public void updateUserAddress(UserAddressDto userAddressDto) {
        UserAddress userAddress = userAddressRepository.findById(userAddressDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("UserAddress with id" + userAddressDto.getId() + "does not exist"));
        userAddress.setCountry(userAddressDto.getCountry());
        userAddress.setCity(userAddressDto.getCity());
        userAddress.setStreet(userAddressDto.getStreet());
        userAddress.setBuildingNumber(userAddressDto.getBuildingNumber());
        userAddress.setLocalNumber(userAddressDto.getLocalNumber());
        userAddress.setPostCode(userAddressDto.getPostCode());
        mapper.userAddressMapToDto(userAddressRepository.save(userAddress));
    }

    public void partialUpdateUserAddress (UserAddressDto userAddressDto) {
        UserAddress userAddress = userAddressRepository.findById(userAddressDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("UserAddress with id" + userAddressDto.getId() + "does not exist"));
        if (userAddressDto.getCountry() !=null) {
            userAddress.setCountry(userAddressDto.getCountry());
        }
        if (userAddressDto.getCity() != null) {
            userAddress.setCity(userAddressDto.getCity());
        }
        if (userAddressDto.getStreet() !=null) {
            userAddress.setStreet(userAddressDto.getStreet());
        }
        if (userAddressDto.getBuildingNumber() != null) {
            userAddress.setBuildingNumber(userAddressDto.getBuildingNumber());
        }
        if (userAddressDto.getLocalNumber() != null) {
            userAddress.setLocalNumber(userAddressDto.getLocalNumber());
        }
        if (userAddressDto.getPostCode() != null) {
            userAddress.setPostCode(userAddressDto.getPostCode());
        }
        mapper.userAddressMapToDto(userAddressRepository.save(userAddress));
    }
}
