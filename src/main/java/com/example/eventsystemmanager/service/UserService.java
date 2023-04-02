package com.example.eventsystemmanager.service;

import com.example.eventsystemmanager.dto.UserAddressDto;
import com.example.eventsystemmanager.dto.UserDto;
import com.example.eventsystemmanager.entity.UserEntity;
import com.example.eventsystemmanager.entity.UserAddressEntity;
import com.example.eventsystemmanager.mapper.UserMapper;
import com.example.eventsystemmanager.repository.UserAddressRepository;
import com.example.eventsystemmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final String USERADDRESSWITHIDSTATEMENT = "UserAddress with id " + id + " does not exist.";
    private final UserAddressRepository userAddressRepository;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private UserDto toDto(UserEntity userEntity) {
        return  UserDto.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUserName())
                .userSurname(userEntity.getUserSurname())
                .login(userEntity.getLogin())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .userAddress(new UserAddressDto())
                .build();
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::userMapToDto)
                .orElseThrow(() -> new IllegalArgumentException(USERADDRESSWITHIDSTATEMENT));
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
            UserEntity userEntity = userDto.toUser();
            userAddressRepository.save(userEntity.getUserAddressEntity());
            userRepository.save(userEntity);
        }
    }

    public void updateUser(UserDto userDto) {
        if (userRepository.findById(userDto.getId()).isEmpty()) {
            throw new IllegalArgumentException(USERADDRESSWITHIDSTATEMENT);
        } else if (userAddressRepository.findById(userDto.getUserAddress().getId()).isEmpty()) {
            throw new IllegalArgumentException("User address with id " + userDto.getUserAddress().getId() + " does not exist");
        } else {
            userAddressRepository.save(userDto.getUserAddress().toUserAddress());
        }
        userRepository.save(userDto.toUser());
    }

    public void partialUpdateUser(UserDto userDto) {
        UserEntity userEntity = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userDto.getId() + " does not exist"));
        if (userDto.getUserName() != null) {
            userEntity.setUserName(userDto.getUserName());
        }
        if (userDto.getUserSurname() != null) {
            userEntity.setUserSurname(userDto.getUserSurname());
        }
        if (userDto.getLogin() != null) {
            userEntity.setLogin(userDto.getLogin());
        }
        if (userDto.getPassword() != null) {
            userEntity.setPassword(userDto.getPassword());
        }
        if (userDto.getEmail() != null) {
            userEntity.setEmail(userDto.getEmail());
        }
        if (userDto.getPhone() != null) {
            userEntity.setPhone(userDto.getPhone());
        }
        userAddressUpdate(userDto, userEntity);
        userRepository.save(userEntity);
    }

    @CheckForNull
    private void userAddressUpdate(UserDto userDto, UserEntity userEntity) {
        if (userDto.getUserAddress() != null) {
            UserAddressEntity userAddressEntity = userAddressRepository.findById(userDto.getUserAddress().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User address with id " + userDto.getUserAddress().getId() + " does not exist"));
            if (userAddressEntity != null) {
                userEntity.setUserAddressEntity(userAddressEntity);
            }
            if (userDto.getUserAddress().getCountry() != null) {
                userAddressEntity.setCountry(userDto.getUserAddress().getCountry());
            }
            if (userDto.getUserAddress().getCity() != null) {
                userAddressEntity.setCity(userDto.getUserAddress().getCity());
            }
            if (userDto.getUserAddress().getStreet() != null) {
                userAddressEntity.setStreet(userDto.getUserAddress().getStreet());
            }
            if (userDto.getUserAddress().getBuildingNumber() != null) {
                userAddressEntity.setBuildingNumber(userDto.getUserAddress().getBuildingNumber());
            }
            if (userDto.getUserAddress().getLocalNumber() != null) {
                userAddressEntity.setLocalNumber(userDto.getUserAddress().getLocalNumber());
            }
            if (userDto.getUserAddress().getPostCode() != null) {
                userAddressEntity.setPostCode(userDto.getUserAddress().getPostCode());
            }
        }
    }

    public void removeUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User  with ID " + id + " not found."));
        userRepository.delete(userEntity);
    }
}



















