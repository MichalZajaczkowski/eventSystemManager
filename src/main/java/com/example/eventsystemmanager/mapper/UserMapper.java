package com.example.eventsystemmanager.mapper;

import com.example.eventsystemmanager.dto.UserDto;
import com.example.eventsystemmanager.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userMapToDto(User user);
}
