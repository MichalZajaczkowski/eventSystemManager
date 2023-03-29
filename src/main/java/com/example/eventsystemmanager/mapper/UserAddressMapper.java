package com.example.eventsystemmanager.mapper;

import com.example.eventsystemmanager.dto.UserAddressDto;
import com.example.eventsystemmanager.entity.UserAddressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {

    UserAddressDto userAddressMapToDto(UserAddressEntity userAddressEntity);
}
