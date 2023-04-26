package com.example.eventsystemmanager.place;


import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.AddressMapper;
import com.example.eventsystemmanager.address.AddressRepository;
import com.example.eventsystemmanager.address.addressType.AddressType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
