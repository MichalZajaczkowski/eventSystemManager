package com.example.eventsystemmanager.mapper;

import com.example.eventsystemmanager.dto.UserAddressDto;
import com.example.eventsystemmanager.entity.UserAddress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {

    UserAddressDto userAddressMapToDto(UserAddress userAddress);
}
