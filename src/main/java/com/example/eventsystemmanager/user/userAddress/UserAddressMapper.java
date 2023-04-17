package com.example.eventsystemmanager.user.userAddress;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {

    UserAddressDto userAddressMapToDto(UserAddressEntity userAddressEntity);
}
