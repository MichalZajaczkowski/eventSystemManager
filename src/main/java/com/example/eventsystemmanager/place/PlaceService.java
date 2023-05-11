package com.example.eventsystemmanager.place;


import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.AddressMapper;
import com.example.eventsystemmanager.address.AddressRepository;
import com.example.eventsystemmanager.address.addressType.AddressType;
import com.example.eventsystemmanager.exception.DuplicateEntityException;
import com.example.eventsystemmanager.user.UserEntity;
import com.example.eventsystemmanager.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.NonUniqueResultException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Service
public class PlaceService {

    private static final String PLACE_WITH_ID_DOES_NOT_EXIST = "Place with id " + id + " does not exist.";
    private final AddressRepository placeAddressRepository;
    private final AddressMapper userAddressMapper;
    private final PlaceRepository placeRepository;

    private final AddressMapper addressMapper;
    @Autowired
    private UserService userService;

    public PlaceDto createPlace(PlaceDto placeDto) {
        PlaceEntity placeEntity = placeDto.toPlaceEntity();
        if (placeRepository.findByName(placeDto.getName()) != null) {
            throw new IllegalArgumentException("Place with name '" + placeDto.getName() + "' already exists");
        }
        if (placeDto.getPlaceAddress() != null) {
            AddressDto addressDto = placeDto.getPlaceAddress();

            AddressEntity existingAddress = placeAddressRepository.findByAddressFields(
                    addressDto.getCountry(), addressDto.getCity(), addressDto.getStreet(),
                    addressDto.getBuildingNumber(), addressDto.getLocalNumber(), addressDto.getPostCode(),
                    AddressType.PLACE_ADDRESS);

            if (existingAddress != null) {
                placeEntity.setPlaceAddressEntity(existingAddress);
            } else {
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

    public Map<String, Object> partialUpdatePlaceData(Long placeId, PlaceDto placeDto) {
        PlaceEntity placeToUpdate = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalStateException(PLACE_WITH_ID_DOES_NOT_EXIST));

        Map<String, Object> changedFields = new HashMap<>();

        if (placeDto.getName() != null) {
            placeToUpdate.setName(placeDto.getName());
            changedFields.put("name", placeDto.getName());
        }

        if (placeDto.getShortName() != null) {
            placeToUpdate.setShortName(placeDto.getShortName());
            changedFields.put("shortName", placeDto.getShortName());
        }

        if (placeDto.getDescription() != null) {
            placeToUpdate.setDescription(placeDto.getDescription());
            changedFields.put("description", placeDto.getDescription());
        }

        if (placeDto.getQuantityAvailablePlaces() != null) {
            placeToUpdate.setQuantityAvailablePlaces(placeDto.getQuantityAvailablePlaces());
            changedFields.put("quantityAvailablePlaces", placeDto.getQuantityAvailablePlaces());
        }

        placeToUpdate.setModifiedDate(LocalDateTime.now());
        placeRepository.save(placeToUpdate);

        return changedFields;
    }


    public AddressEntity updateAddressForPlace(Long placeId, AddressDto addressDto) {
        PlaceEntity placeAddressToUpdate = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException(PLACE_WITH_ID_DOES_NOT_EXIST));

        AddressEntity newAddress = addressMapper.addressMapToEntity(addressDto);
        // Sprawdź czy inny użytkownik nie ma już przypisanego tego samego adresu
        newAddress.setAddressType(AddressType.PLACE_ADDRESS);
        newAddress = userService.checkIfAnotherUserIsAlreadyAssignedTheSameAddress(newAddress);
        // Jeśli id adresu nie zostało podane, to zapisz nowy adres
        newAddress.setAddressType(AddressType.PLACE_ADDRESS);
        newAddress = userService.ifTheAddressIdIsNotGivenSaveTheNewAddress(addressDto, newAddress);
        // Aktualizuj adres tylko dla aktualizowanego użytkownika
        placeAddressToUpdate.setPlaceAddressEntity(newAddress);
        placeRepository.save(placeAddressToUpdate);
        return newAddress;
    }

    public void removePlace(Long id) {
        PlaceEntity placeToRemove = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(PLACE_WITH_ID_DOES_NOT_EXIST));
        placeRepository.delete(placeToRemove);
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
