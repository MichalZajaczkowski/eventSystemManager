package com.example.eventsystemmanager.place;

import com.example.eventsystemmanager.user.userAddress.UserAddressDto;
import com.example.eventsystemmanager.user.userAddress.UserAddressEntity;
import com.example.eventsystemmanager.user.userAddress.UserAddressMapper;
import com.example.eventsystemmanager.user.userAddress.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlaceService {
    private final UserAddressRepository userAddressRepository;
    private final UserAddressMapper userAddressMapper;
    private final PlaceRepository placeRepository;

    public PlaceDto createPlace(PlaceDto placeDto) {
        PlaceEntity placeEntity = placeDto.toPlaceEntity();

        if(placeDto.getUserAddress() != null) {
            UserAddressDto userAddressDto = placeDto.getUserAddress();

            // Check if an address with the same fields already exists in the database
            UserAddressEntity existingAddress = userAddressRepository.findByIdOrFindByAddressFields(userAddressDto.getId(),
                    userAddressDto.getCountry(), userAddressDto.getCity(), userAddressDto.getStreet(), userAddressDto.getBuildingNumber(),
                    userAddressDto.getLocalNumber(), userAddressDto.getPostCode());

            if (existingAddress != null) {
                // Set the existing user address id for the user
                placeEntity.setUserAddressEntity(existingAddress);
            } else {
                // Create a new user address with a new id
                UserAddressEntity newAddress = userAddressMapper.userAddressMapToEntity(userAddressDto);
                newAddress = userAddressRepository.save(newAddress);
                placeEntity.setUserAddressEntity(newAddress);
            }
        }

        placeRepository.save(placeEntity);
        return placeDto;
    }
}
