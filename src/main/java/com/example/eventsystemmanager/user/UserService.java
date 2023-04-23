package com.example.eventsystemmanager.user;

import com.example.eventsystemmanager.status.StatusRepository;
import com.example.eventsystemmanager.user.userAddress.UserAddressDto;
import com.example.eventsystemmanager.user.userAddress.UserAddressEntity;
import com.example.eventsystemmanager.user.userAddress.UserAddressMapper;
import com.example.eventsystemmanager.user.userAddress.UserAddressRepository;
import com.example.eventsystemmanager.user.userStatus.UserStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NamingConventions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
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

    private final UserAddressMapper userAddressMapper;
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
        UserEntity userEntity = userDto.toUserEntity();

        if (userDto.getUserAddress() != null) {
            UserAddressDto userAddressDto = userDto.getUserAddress();

            // Check if an address with the same fields already exists in the database
            UserAddressEntity existingAddress = userAddressRepository.findByIdOrFindByAddressFields(userAddressDto.getId(),
                    userAddressDto.getCountry(), userAddressDto.getCity(), userAddressDto.getStreet(), userAddressDto.getBuildingNumber(),
                    userAddressDto.getLocalNumber(), userAddressDto.getPostCode());

            if (existingAddress != null) {
                // Set the existing user address id for the user
                userEntity.setUserAddressEntity(existingAddress);
            } else {
                // Create a new user address with a new id
                UserAddressEntity newAddress = userAddressMapper.userAddressMapToEntity(userAddressDto);
                newAddress = userAddressRepository.save(newAddress);
                userEntity.setUserAddressEntity(newAddress);
            }
        }

        userRepository.save(userEntity);
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
        userRepository.save(userDto.toUserEntity());
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
        userRepository.save(userEntity);
    }

    public UserAddressEntity updateAddressForUser(Long userId, UserAddressDto userAddressDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika o podanym id: " + userId));

        UserAddressEntity newAddress = userAddressMapper.userAddressMapToEntity(userAddressDto);
        // Sprawdź czy inny użytkownik nie ma już przypisanego tego samego adresu
        newAddress = checkIfAnotherUserIsAlreadyAssignedTheSameAddress(newAddress);
        // Jeśli id adresu nie zostało podane, to zapisz nowy adres
        newAddress = ifTheAddressIdIsNotGivenSaveTheNewAddress(userAddressDto, newAddress);
        // Aktualizuj adres tylko dla aktualizowanego użytkownika
        user.setUserAddressEntity(newAddress);
        userRepository.save(user);
        return newAddress;
    }

    private UserAddressEntity ifTheAddressIdIsNotGivenSaveTheNewAddress(UserAddressDto userAddressDto, UserAddressEntity newAddress) {
        if (newAddress.getId() == null) {
            newAddress = userAddressRepository.save(newAddress);
        } else { // W przeciwnym wypadku, zaktualizuj istniejący adres
            Optional<UserAddressEntity> existingAddressId = userAddressRepository.findById(newAddress.getId());
            if (existingAddressId.isPresent()) {
                newAddress = existingAddressId.get();
            } else {
                throw new IllegalArgumentException("Nie znaleziono adresu o podanym id: " + newAddress.getId());
            }
            newAddress.updateFieldsFromDto(userAddressDto); // Metoda w UserAddressEntity aktualizująca pola na podstawie DTO
        }
        return newAddress;
    }

    private UserAddressEntity checkIfAnotherUserIsAlreadyAssignedTheSameAddress(UserAddressEntity newAddress) {
        UserAddressEntity existingAddress = userAddressRepository.findByAddressFields(newAddress.getCountry(), newAddress.getCity(), newAddress.getStreet(), newAddress.getBuildingNumber(), newAddress.getLocalNumber(), newAddress.getPostCode());
        if (existingAddress != null) {
            newAddress = existingAddress;
        } else {
            newAddress = userAddressRepository.save(newAddress);
        }
        return newAddress;
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

    private UserAddressEntity mapToUserAddressEntity(UserAddressDto addressDto) {
        return UserAddressEntity.builder()
                .id(addressDto.getId())
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .street(addressDto.getStreet())
                .buildingNumber(addressDto.getBuildingNumber())
                .localNumber(addressDto.getLocalNumber())
                .postCode(addressDto.getPostCode())
                .build();
    }

    private UserDto mapUserToDtoSimple(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
//        userDto.setUserAddressToDtoId(userEntity.getUserAddressEntityId());
        userDto.setUserStatus(userEntity.getUserStatus());
        return userDto;
    }
}