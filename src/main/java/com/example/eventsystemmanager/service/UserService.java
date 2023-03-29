package com.example.eventsystemmanager.service;

import com.example.eventsystemmanager.dto.UserAddressDto;
import com.example.eventsystemmanager.dto.UserDto;
import com.example.eventsystemmanager.entity.User;
import com.example.eventsystemmanager.entity.UserAddress;
import com.example.eventsystemmanager.mapper.UserMapper;
import com.example.eventsystemmanager.repository.UserAddressRepository;
import com.example.eventsystemmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private UserDto toDto(User user) {
        return  UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .userSurname(user.getUserSurname())
                .login(user.getLogin())
                .password(user.getPassword())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userAddress(new UserAddressDto())
                .build();
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::userMapToDto)
                .orElseThrow(() -> new IllegalArgumentException("UserAddress with id " + id + " does not exist."));
    }

    public void createUser(UserDto userDto) {
        if (userDto.getUserAddress() != null && userDto.getUserAddress().getId() != null) {
            if (userAddressRepository.findById(userDto.getUserAddress().getId()).isEmpty()) {
                throw new IllegalArgumentException("User address with id " + userDto.getUserAddress().getId() + " does not exist");
            }
            Long id = userDto.getUserAddress().getId();
            userAddressRepository.findById(id)
                    .ifPresent(userAddress -> {
                                userDto.setUserAddressToDto(userAddress);
                                userRepository.save(userDto.toUser());
                            }
                    );
        } else {
            User user = userDto.toUser();
            userAddressRepository.save(user.getUserAddress());
            userRepository.save(user);
        }
    }

    public void updateUser(UserDto userDto) {
        if (userRepository.findById(userDto.getId()).isEmpty()) {
            throw new IllegalArgumentException("User with id " + userDto.getId() + " does not exist");
        } else if (userAddressRepository.findById(userDto.getUserAddress().getId()).isEmpty()) {
            throw new IllegalArgumentException("User address with id " + userDto.getUserAddress().getId() + " does not exist");
        } else {
            userAddressRepository.save(userDto.getUserAddress().toUserAddress());
        }
        userRepository.save(userDto.toUser());
    }

    public void partialUpdateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userDto.getId() + " does not exist"));
        if (userDto.getUserName() != null) {
            user.setUserName(userDto.getUserName());
        }
        if (userDto.getUserSurname() != null) {
            user.setUserSurname(userDto.getUserSurname());
        }
        if (userDto.getLogin() != null) {
            user.setLogin(userDto.getLogin());
        }
        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
        }
        if (userDto.getUserAddress() != null) {
            UserAddress userAddress = userAddressRepository.findById(userDto.getUserAddress().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User address with id " + userDto.getUserAddress().getId() + " does not exist"));
            if (userAddress != null) {
                user.setUserAddress(userAddress);
            }
            if (userDto.getUserAddress().getCountry() != null) {
                userAddress.setCountry(userDto.getUserAddress().getCountry());
            }
            if (userDto.getUserAddress().getCity() != null) {
                userAddress.setCity(userDto.getUserAddress().getCity());
            }
            if (userDto.getUserAddress().getStreet() != null) {
                userAddress.setStreet(userDto.getUserAddress().getStreet());
            }
            if (userDto.getUserAddress().getBuildingNumber() != null) {
                userAddress.setBuildingNumber(userDto.getUserAddress().getBuildingNumber());
            }
            if (userDto.getUserAddress().getLocalNumber() != null) {
                userAddress.setLocalNumber(userDto.getUserAddress().getLocalNumber());
            }
            if (userDto.getUserAddress().getPostCode() != null) {
                userAddress.setPostCode(userDto.getUserAddress().getPostCode());
            }
        }
        userRepository.save(user);
    }

    public void removeUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User  with ID " + id + " not found."));
        userRepository.delete(user);
    }
}



















