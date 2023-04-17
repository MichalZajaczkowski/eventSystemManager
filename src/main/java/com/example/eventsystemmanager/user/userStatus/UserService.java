package com.example.eventsystemmanager.user.userStatus;

import com.example.eventsystemmanager.status.StatusRepository;
import com.example.eventsystemmanager.user.UserDto;
import com.example.eventsystemmanager.user.UserEntity;
import com.example.eventsystemmanager.user.UserMapper;
import com.example.eventsystemmanager.user.UserRepository;
import com.example.eventsystemmanager.user.userAddress.UserAddressEntity;
import com.example.eventsystemmanager.user.userAddress.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NamingConventions;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final String USERADDRESSWITHIDSTATEMENT = "UserAddress with id " + id + " does not exist.";
    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;

    private final StatusRepository statusRepository;
    private final UserMapper userMapper;
    private final ModelMapper modelMapper;
    @PostConstruct
    public void configureMapper() {
        modelMapper.getConfiguration()
                .setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR)
                .setDestinationNamingConvention(NamingConventions.JAVABEANS_ACCESSOR)
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true);
    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::mapUserToDto)
                .toList();
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(this::mapUserToDto)
                .orElseThrow(() -> new IllegalArgumentException(USERADDRESSWITHIDSTATEMENT));
    }

    public UserDto createUser(UserDto userDto) {
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
        return userDto;
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

    public void setStatus(UserDto userDto, UserStatus userStatus) {
        UserEntity user = userRepository.findById(userDto.getId()).orElseThrow(() -> new IllegalArgumentException("User not found."));
        user.setUserStatus(userStatus);
        userRepository.save(user);
    }
    public List<UserDto> getUsersByStatusName(String statusName) {
        UserStatus userStatus = UserStatus.fromName(statusName);
        List<UserEntity> users = userRepository.findByUserStatus(userStatus);
        return users.stream()
                .map(this::mapUserToDtoSimple)
                .collect(Collectors.toList());
    }
    public List<UserDto> getUsersByStatusValue(Integer statusValue) {
        UserStatus userStatus = UserStatus.fromValue(statusValue);
        List<UserEntity> users = userRepository.findByUserStatus(userStatus);
        return users.stream()
                .map(this::mapUserToDtoSimple)
                .collect(Collectors.toList());
    }


    private UserDto mapUserToDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setUserName(userEntity.getUserName());
        userDto.setUserSurname(userEntity.getUserSurname());
        userDto.setLogin(userEntity.getLogin());
        userDto.setPassword(userEntity.getPassword());
        userDto.setEmail(userEntity.getEmail());
        userDto.setPhone(userEntity.getPhone());
        userDto.setUserAddressToDto(userEntity.getUserAddressEntity());
        userDto.setUserStatus(userEntity.getUserStatus());
        return userDto;
    }

    private UserDto mapUserToDtoSimple(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setUserAddressToDto(userEntity.getUserAddressEntityId());
        userDto.setUserStatus(userEntity.getUserStatus());
        return userDto;
    }
}