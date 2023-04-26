package com.example.eventsystemmanager.user;

import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.AddressMapper;
import com.example.eventsystemmanager.status.StatusRepository;
import com.example.eventsystemmanager.address.AddressRepository;
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
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    private final StatusRepository statusRepository;
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
            AddressDto addressDto = userDto.getUserAddress();

            // Check if an address with the same fields already exists in the database
            AddressEntity existingAddress = addressRepository.findByIdOrFindByAddressFields(addressDto.getId(),
                    addressDto.getCountry(), addressDto.getCity(), addressDto.getStreet(), addressDto.getBuildingNumber(),
                    addressDto.getLocalNumber(), addressDto.getPostCode());

            if (existingAddress != null) {
                // Set the existing user address id for the user
                userEntity.setAddressEntity(existingAddress);
            } else {
                // Create a new user address with a new id
                AddressEntity newAddress = addressMapper.addressMapToEntity(addressDto);
                newAddress = addressRepository.save(newAddress);
                userEntity.setAddressEntity(newAddress);
            }
        }

        userRepository.save(userEntity);
        return userDto;
    }


    public void updateUser(UserDto userDto) {
        if (userRepository.findById(userDto.getId()).isEmpty()) {
            throw new IllegalArgumentException(USERADDRESSWITHIDSTATEMENT);
        } else if (addressRepository.findById(userDto.getUserAddress().getId()).isEmpty()) {
            throw new IllegalArgumentException("User address with id " + userDto.getUserAddress().getId() + " does not exist");
        } else {
            addressRepository.save(userDto.getUserAddress().toAddress());
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

    public AddressEntity updateAddressForUser(Long userId, AddressDto addressDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika o podanym id: " + userId));

        AddressEntity newAddress = addressMapper.addressMapToEntity(addressDto);
        // Sprawdź czy inny użytkownik nie ma już przypisanego tego samego adresu
        newAddress = checkIfAnotherUserIsAlreadyAssignedTheSameAddress(newAddress);
        // Jeśli id adresu nie zostało podane, to zapisz nowy adres
        newAddress = ifTheAddressIdIsNotGivenSaveTheNewAddress(addressDto, newAddress);
        // Aktualizuj adres tylko dla aktualizowanego użytkownika
        user.setAddressEntity(newAddress);
        userRepository.save(user);
        return newAddress;
    }

    private AddressEntity ifTheAddressIdIsNotGivenSaveTheNewAddress(AddressDto addressDto, AddressEntity newAddress) {
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

    private AddressEntity checkIfAnotherUserIsAlreadyAssignedTheSameAddress(AddressEntity newAddress) {
        AddressEntity existingAddress = addressRepository.findByAddressFields(newAddress.getCountry(), newAddress.getCity(), newAddress.getStreet(), newAddress.getBuildingNumber(), newAddress.getLocalNumber(), newAddress.getPostCode());
        if (existingAddress != null) {
            newAddress = existingAddress;
        } else {
            newAddress = addressRepository.save(newAddress);
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
        userDto.setUserAddressToDto(userEntity.getAddressEntity());
        userDto.setUserStatus(userEntity.getUserStatus());
        return userDto;
    }

    private AddressEntity mapToUserAddressEntity(AddressDto addressDto) {
        return AddressEntity.builder()
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