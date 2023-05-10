package com.example.eventsystemmanager.user;

import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.AddressMapper;
import com.example.eventsystemmanager.address.addressType.AddressType;
import com.example.eventsystemmanager.status.StatusRepository;
import com.example.eventsystemmanager.address.AddressRepository;
import com.example.eventsystemmanager.user.userStatus.UserStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NamingConventions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final String USER_WITH_ID = "User with id ";
    private static final String DOES_NOT_EXIST = " does not exist.";
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
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
                .map(userMapper::userMapToDto)
                .toList();
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::userMapToDto)
                .orElseThrow(() -> new IllegalArgumentException(USER_WITH_ID + id + DOES_NOT_EXIST));
    }

    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = userDto.toUserEntity();

        if (userDto.getUserAddress() != null) {
            userEntity.setAddressEntity(processUserAddress(userDto.getUserAddress()));
        }

        UserEntity savedUserEntity = userRepository.save(userEntity);
        if (savedUserEntity == null) {
            throw new RuntimeException("Failed to save user.");
        }

        return UserDto.fromUserEntity(savedUserEntity);
    }

    private AddressEntity processUserAddress(AddressDto addressDto) {
        // Check if an address with the same fields already exists in the database
        AddressEntity existingAddress = addressRepository.findByAddressFields(
                addressDto.getCountry(), addressDto.getCity(), addressDto.getStreet(),
                addressDto.getBuildingNumber(), addressDto.getLocalNumber(), addressDto.getPostCode(),
                AddressType.USER_ADDRESS);

        if (existingAddress != null) {
            // An address with the same fields already exists, so return it
            return existingAddress;
        } else {
            // No address with the same fields exists, so create a new one
            AddressEntity newAddress = addressMapper.addressMapToEntity(addressDto);
            newAddress.setAddressType(AddressType.USER_ADDRESS);
            return addressRepository.save(newAddress);
        }
    }

    public Map<String, Object> partialUpdateUser(UserDto userDto) {
        UserEntity userEntity = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userDto.getId() + " does not exist"));
        Map<String, Object> changedFields = new HashMap<>();

        if (userDto.getUserName() != null) {
            userEntity.setUserName(userDto.getUserName());
            changedFields.put("userName", userDto.getUserName());
        }
        if (userDto.getUserSurname() != null) {
            userEntity.setUserSurname(userDto.getUserSurname());
            changedFields.put("userSurname", userDto.getUserSurname());
        }
        if (userDto.getLogin() != null) {
            userEntity.setLogin(userDto.getLogin());
            changedFields.put("login", userDto.getLogin());
        }
        if (userDto.getPassword() != null) {
            userEntity.setPassword(userDto.getPassword());
            changedFields.put("password", userDto.getPassword());
        }
        if (userDto.getEmail() != null) {
            userEntity.setEmail(userDto.getEmail());
            changedFields.put("email", userDto.getEmail());
        }
        if (userDto.getPhone() != null) {
            userEntity.setPhone(userDto.getPhone());
            changedFields.put("phone", userDto.getPhone());
        }
        userEntity.setModifiedDate(LocalDateTime.now());
        userRepository.save(userEntity);
        return changedFields;
    }


    public AddressEntity updateAddressForUser(Long userId, AddressDto addressDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika o podanym id: " + userId));

        AddressEntity newAddress = addressMapper.addressMapToEntity(addressDto);
        // Sprawdź czy inny użytkownik nie ma już przypisanego tego samego adresu
        newAddress.setAddressType(AddressType.USER_ADDRESS);
        newAddress = checkIfAnotherUserIsAlreadyAssignedTheSameAddress(newAddress);
        // Jeśli id adresu nie zostało podane, to zapisz nowy adres
        newAddress.setAddressType(AddressType.USER_ADDRESS);
        newAddress = ifTheAddressIdIsNotGivenSaveTheNewAddress(addressDto, newAddress);
        // Aktualizuj adres tylko dla aktualizowanego użytkownika
        user.setAddressEntity(newAddress);
        userRepository.save(user);
        return newAddress;
    }

    public AddressEntity checkIfAnotherUserIsAlreadyAssignedTheSameAddress(AddressEntity newAddress) {
        AddressEntity existingAddress = addressRepository.findByAddressFields(newAddress.getCountry(),
                newAddress.getCity(), newAddress.getStreet(), newAddress.getBuildingNumber(),
                newAddress.getLocalNumber(), newAddress.getPostCode(), newAddress.getAddressType());
        if (existingAddress != null) {
            newAddress = existingAddress;
        } else {
            newAddress = addressRepository.save(newAddress);
        }
        return newAddress;
    }

    public AddressEntity ifTheAddressIdIsNotGivenSaveTheNewAddress(AddressDto addressDto, AddressEntity newAddress) {
        if (newAddress.getId() == null) {
            newAddress = addressRepository.save(newAddress);
        } else { // W przeciwnym wypadku, zaktualizuj istniejący adres
            Optional<AddressEntity> existingAddressId = addressRepository.findById(newAddress.getId());
            if (existingAddressId.isPresent()) {
                newAddress = existingAddressId.get();
            } else {
                throw new IllegalArgumentException("Nie znaleziono adresu o podanym id: " + newAddress.getId());
            }
            newAddress.updateFieldsFromDto(addressDto); // Metoda w UserAddressEntity aktualizująca pola na podstawie DTO
        }
        return newAddress;
    }

    public void removeUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User  with ID " + id + " not found."));
        userRepository.delete(userEntity);
    }

    public void setStatus(UserDto userDto, UserStatus userStatus) {
        UserEntity user = userRepository.findById(userDto.getId()).orElseThrow(() ->
                new IllegalArgumentException("User not found."));
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
    private UserDto mapUserToDtoSimple(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
//        userDto.setUserAddressToDtoId(userEntity.getUserAddressEntityId());
        userDto.setUserStatus(userEntity.getUserStatus());
        return userDto;
    }
}