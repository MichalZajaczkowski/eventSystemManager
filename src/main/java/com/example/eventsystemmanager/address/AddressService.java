package com.example.eventsystemmanager.address;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@RequiredArgsConstructor
@Slf4j
@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public List<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    private AddressDto toDto(AddressEntity addressEntity) {
        return AddressDto.builder()
                .id(addressEntity.getId())
                .country(addressEntity.getCountry())
                .city(addressEntity.getCity())
                .street(addressEntity.getStreet())
                .buildingNumber(addressEntity.getBuildingNumber())
                .localNumber(addressEntity.getLocalNumber())
                .postCode(addressEntity.getPostCode())
                .build();

    }

    public AddressDto findById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::addressMapToDto)
                .orElseThrow(() -> new IllegalArgumentException("UserAddress with id " + id + " does not exist."));
    }

    public void createUserAddress(AddressDto addressDto) {
        if (addressRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("UserAddress with id " + id + " already exists");
        } else {
            addressRepository.save(addressDto.toAddress());
        }
    }

    public void updateUserAddress(AddressDto addressDto) {
        AddressEntity addressEntity = addressRepository.findById(addressDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("UserAddress with id" + addressDto.getId() + "does not exist"));
        addressEntity.setCountry(addressDto.getCountry());
        addressEntity.setCity(addressDto.getCity());
        addressEntity.setStreet(addressDto.getStreet());
        addressEntity.setBuildingNumber(addressDto.getBuildingNumber());
        addressEntity.setLocalNumber(addressDto.getLocalNumber());
        addressEntity.setPostCode(addressDto.getPostCode());
        addressMapper.addressMapToDto(addressRepository.save(addressEntity));
    }

    public void partialUpdateUserAddress(AddressDto addressDto) {
        AddressEntity addressEntity = addressRepository.findById(addressDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("UserAddress with id" + addressDto.getId() + "does not exist"));
        if (addressDto.getCountry() != null) {
            addressEntity.setCountry(addressDto.getCountry());
        }
        if (addressDto.getCity() != null) {
            addressEntity.setCity(addressDto.getCity());
        }
        if (addressDto.getStreet() != null) {
            addressEntity.setStreet(addressDto.getStreet());
        }
        if (addressDto.getBuildingNumber() != null) {
            addressEntity.setBuildingNumber(addressDto.getBuildingNumber());
        }
        if (addressDto.getLocalNumber() != null) {
            addressEntity.setLocalNumber(addressDto.getLocalNumber());
        }
        if (addressDto.getPostCode() != null) {
            addressEntity.setPostCode(addressDto.getPostCode());
        }
        addressRepository.save(addressEntity);
    }

    public void removeAddress(Long id) {
        AddressEntity addressEntity = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User address with ID " + id + " not found."));
        addressRepository.delete(addressEntity);
    }
}