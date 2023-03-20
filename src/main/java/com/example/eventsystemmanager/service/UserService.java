package com.example.eventsystemmanager.service;

import com.example.eventsystemmanager.dto.UserDto;
import com.example.eventsystemmanager.entity.User;
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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        } else {
            return userRepository.findById(id);
        }
    }

    public void createUser (UserDto userDto) {
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
}
