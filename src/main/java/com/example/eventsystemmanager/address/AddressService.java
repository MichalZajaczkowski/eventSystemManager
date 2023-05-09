package com.example.eventsystemmanager.address;

import com.example.eventsystemmanager.exception.AddressCreationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .addressType(addressEntity.getAddressType())
                .createdDate(addressEntity.getCreatedDate())
                .modifiedDate(addressEntity.getModifiedDate())
                .build();

    }

    public AddressDto findById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::addressMapToDto)
                .orElseThrow(() -> new IllegalArgumentException("Address with id " + id + " does not exist."));
    }

    public void createAddress(AddressDto addressDto) {
        AddressEntity addressEntity = addressDto.toAddressEntity();

        AddressEntity savedAddressEntity = addressRepository.save(addressEntity);
        if (savedAddressEntity == null || savedAddressEntity.getId() == null) {
            throw new AddressCreationException("Address could not be created");
        }

        addressDto.setId(savedAddressEntity.getId());
    }

    public Map<String, Object> partialUpdateAddress(Long id, AddressDto addressDto) {
        AddressEntity addressEntity = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address with id " + id + " does not exist"));
        Map<String, Object> changedFields = new HashMap<>();

        if (addressDto.getCountry() != null) {
            addressEntity.setCountry(addressDto.getCountry());
            changedFields.put("country", addressDto.getCountry());
        }
        if (addressDto.getCity() != null) {
            addressEntity.setCity(addressDto.getCity());
            changedFields.put("city", addressDto.getCity());
        }
        if (addressDto.getStreet() != null) {
            addressEntity.setStreet(addressDto.getStreet());
            changedFields.put("street", addressDto.getStreet());
        }
        if (addressDto.getBuildingNumber() != null) {
            addressEntity.setBuildingNumber(addressDto.getBuildingNumber());
            changedFields.put("buildingNumber", addressDto.getBuildingNumber());
        }
        if (addressDto.getLocalNumber() != null) {
            addressEntity.setLocalNumber(addressDto.getLocalNumber());
            changedFields.put("localNumber", addressDto.getLocalNumber());
        }
        if (addressDto.getPostCode() != null) {
            addressEntity.setPostCode(addressDto.getPostCode());
            changedFields.put("postCode", addressDto.getPostCode());
        }
        if (addressDto.getAddressType() != null) {
            addressEntity.setAddressType(addressDto.getAddressType());
            changedFields.put("addressType", addressDto.getAddressType());
        }

        addressRepository.save(addressEntity);
        return changedFields;
    }


    public void removeAddress(Long id) {
        AddressEntity addressEntity = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User address with ID " + id + " not found."));
        addressRepository.delete(addressEntity);
    }
}