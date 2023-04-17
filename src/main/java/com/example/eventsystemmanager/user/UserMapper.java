package com.example.eventsystemmanager.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userMapToDto(UserEntity userEntity);
}
