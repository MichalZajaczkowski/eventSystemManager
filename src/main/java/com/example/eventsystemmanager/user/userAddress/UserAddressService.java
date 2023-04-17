package com.example.eventsystemmanager.user.userAddress;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@RequiredArgsConstructor
@Slf4j
@Service
public class UserAddressService {
    private final UserAddressRepository userAddressRepository;
    private final UserAddressMapper userAddressMapper;

    public List<UserAddressDto> findAll() {
        return userAddressRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    private UserAddressDto toDto(UserAddressEntity userAddressEntity) {
        return UserAddressDto.builder()
                .id(userAddressEntity.getId())
                .country(userAddressEntity.getCountry())
                .city(userAddressEntity.getCity())
                .street(userAddressEntity.getStreet())
                .buildingNumber(userAddressEntity.getBuildingNumber())
                .localNumber(userAddressEntity.getLocalNumber())
                .postCode(userAddressEntity.getPostCode())
                .build();

    }

    public UserAddressDto findById(Long id) {
        return userAddressRepository.findById(id)
                .map(userAddressMapper::userAddressMapToDto)
                .orElseThrow(() -> new IllegalArgumentException("UserAddress with id " + id + " does not exist."));
    }

    public void createUserAddress(UserAddressDto userAddressDto) {
        if (userAddressRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("UserAddress with id " + id + " already exists");
        } else {
            userAddressRepository.save(userAddressDto.toUserAddress());
        }
    }

    public void updateUserAddress(UserAddressDto userAddressDto) {
        UserAddressEntity userAddressEntity = userAddressRepository.findById(userAddressDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("UserAddress with id" + userAddressDto.getId() + "does not exist"));
        userAddressEntity.setCountry(userAddressDto.getCountry());
        userAddressEntity.setCity(userAddressDto.getCity());
        userAddressEntity.setStreet(userAddressDto.getStreet());
        userAddressEntity.setBuildingNumber(userAddressDto.getBuildingNumber());
        userAddressEntity.setLocalNumber(userAddressDto.getLocalNumber());
        userAddressEntity.setPostCode(userAddressDto.getPostCode());
        userAddressMapper.userAddressMapToDto(userAddressRepository.save(userAddressEntity));
    }

    public void partialUpdateUserAddress(UserAddressDto userAddressDto) {
        UserAddressEntity userAddressEntity = userAddressRepository.findById(userAddressDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("UserAddress with id" + userAddressDto.getId() + "does not exist"));
        if (userAddressDto.getCountry() != null) {
            userAddressEntity.setCountry(userAddressDto.getCountry());
        }
        if (userAddressDto.getCity() != null) {
            userAddressEntity.setCity(userAddressDto.getCity());
        }
        if (userAddressDto.getStreet() != null) {
            userAddressEntity.setStreet(userAddressDto.getStreet());
        }
        if (userAddressDto.getBuildingNumber() != null) {
            userAddressEntity.setBuildingNumber(userAddressDto.getBuildingNumber());
        }
        if (userAddressDto.getLocalNumber() != null) {
            userAddressEntity.setLocalNumber(userAddressDto.getLocalNumber());
        }
        if (userAddressDto.getPostCode() != null) {
            userAddressEntity.setPostCode(userAddressDto.getPostCode());
        }
        userAddressRepository.save(userAddressEntity);
    }

    public void removeAddress(Long id) {
        UserAddressEntity userAddressEntity = userAddressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User address with ID " + id + " not found."));
        userAddressRepository.delete(userAddressEntity);
    }
}