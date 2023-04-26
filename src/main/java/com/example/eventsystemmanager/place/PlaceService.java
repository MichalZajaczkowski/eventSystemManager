package com.example.eventsystemmanager.place;


import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.AddressMapper;
import com.example.eventsystemmanager.address.AddressRepository;
import com.example.eventsystemmanager.address.addressType.AddressType;
import com.example.eventsystemmanager.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PlaceService {
    private final AddressRepository placeAddressRepository;
    private final AddressMapper userAddressMapper;
    private final PlaceRepository placeRepository;

    public PlaceDto createPlace(PlaceDto placeDto) {
        PlaceEntity placeEntity = placeDto.toPlaceEntity();

        if(placeDto.getPlaceAddress() != null) {
            AddressDto addressDto = placeDto.getPlaceAddress();

            // Check if an address with the same fields already exists in the database
            AddressEntity existingAddress = placeAddressRepository.findByIdOrFindByAddressFields(addressDto.getId(),
                    addressDto.getCountry(), addressDto.getCity(), addressDto.getStreet(), addressDto.getBuildingNumber(),
                    addressDto.getLocalNumber(), addressDto.getPostCode());

            if (existingAddress != null) {
                // Set the existing user address id for the user
                placeEntity.setPlaceAddressEntity(existingAddress);
                existingAddress.setAddressType(AddressType.PLACE_ADDRESS);
            } else {
                // Create a new user address with a new id
                AddressEntity newAddress = userAddressMapper.addressMapToEntity(addressDto);
                newAddress.setAddressType(AddressType.PLACE_ADDRESS);
                newAddress = placeAddressRepository.save(newAddress);
                placeEntity.setPlaceAddressEntity(newAddress);
            }
        }

        placeRepository.save(placeEntity);
        return placeDto;
    }

    public List<PlaceDto> findAll() {
        return placeRepository.findAll()
                .stream()
                .map(this::mapAddressToDto)
                .toList();
    }

    public PlaceDto findById(Long id) {
        return placeRepository.findById(id)
                .map(this::mapAddressToDto)
                .orElseThrow( () -> new IllegalArgumentException("dodać stały tekst do wyjątku"));
    }

    private PlaceDto mapAddressToDto(PlaceEntity placeEntity) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setId(placeEntity.getId());
        placeDto.setName(placeEntity.getName());
        placeDto.setShortName(placeEntity.getShortName());
        placeDto.setDescription(placeEntity.getDescription());
        placeDto.setQuantityAvailablePlaces(placeEntity.getQuantityAvailablePlaces());
        placeDto.setPlaceAddressToDto(placeEntity.getPlaceAddressEntity());
        return placeDto;
    }
}
