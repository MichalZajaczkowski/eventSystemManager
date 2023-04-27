package com.example.eventsystemmanager.place;


import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.AddressMapper;
import com.example.eventsystemmanager.address.AddressRepository;
import com.example.eventsystemmanager.address.addressType.AddressType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Service
public class PlaceService {

    private static final String PLACE_WITH_ID_DOES_NOT_EXIST = "Place with id " + id + " does not exist.";
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
                .orElseThrow(() -> new IllegalArgumentException(PLACE_WITH_ID_DOES_NOT_EXIST));
    }

    public void updatePlaceData(Long placeId, PlaceDto placeDto) {
        PlaceEntity placeToUpdate = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException(PLACE_WITH_ID_DOES_NOT_EXIST));

        placeToUpdate.setName(placeDto.getName());
        placeToUpdate.setShortName(placeDto.getShortName());
        placeToUpdate.setDescription(placeDto.getDescription());
        placeToUpdate.setQuantityAvailablePlaces(placeDto.getQuantityAvailablePlaces());

        placeRepository.save(placeToUpdate);
    }

    public void partialUpdatePlaceData(Long placeId, PlaceDto placeDto) {
        PlaceEntity placeToUpdate = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalStateException(PLACE_WITH_ID_DOES_NOT_EXIST));
        if (placeDto.getName() != null) {
            placeToUpdate.setName(placeDto.getName());
        }
        if (placeDto.getShortName() != null) {
            placeToUpdate.setShortName(placeDto.getShortName());
        }
        if (placeDto.getDescription() != null) {
            placeToUpdate.setDescription(placeDto.getDescription());
        }
        if (placeDto.getQuantityAvailablePlaces() != null) {
            placeToUpdate.setQuantityAvailablePlaces(placeDto.getQuantityAvailablePlaces());
        }
        placeRepository.save(placeToUpdate);
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
