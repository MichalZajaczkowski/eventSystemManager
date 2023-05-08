package com.example.eventsystemmanager.AdditionalEndpoints;

import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.AddressRepository;
import com.example.eventsystemmanager.event.EventDto;
import com.example.eventsystemmanager.event.EventEntity;
import com.example.eventsystemmanager.event.EventMapper;
import com.example.eventsystemmanager.exception.EventNotFoundException;
import com.example.eventsystemmanager.exception.OrganizerNotFoundException;
import com.example.eventsystemmanager.exception.PlaceNotFoundException;
import com.example.eventsystemmanager.organizer.OrganizerEntity;
import com.example.eventsystemmanager.organizer.OrganizerRepository;
import com.example.eventsystemmanager.place.PlaceMapper;
import com.example.eventsystemmanager.place.PlaceRepository;
import com.example.eventsystemmanager.user.UserDto;
import com.example.eventsystemmanager.user.UserEntity;
import com.example.eventsystemmanager.user.UserMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdditionalService {

    private final AdditionalRepository additionalRepository;
    private final OrganizerRepository organizerRepository;
    private final AddressRepository addressRepository;
    private final EventMapper eventMapper;
    private final PlaceMapper placeMapper;
    private final PlaceRepository placeRepository;
    private final UserMapper userMapper;


    public AdditionalService(AdditionalRepository additionalRepository, OrganizerRepository organizerRepository, AddressRepository addressRepository, EventMapper eventMapper, PlaceMapper placeMapper, PlaceRepository placeRepository, UserMapper userMapper) {
        this.additionalRepository = additionalRepository;
        this.organizerRepository = organizerRepository;
        this.addressRepository = addressRepository;
        this.eventMapper = eventMapper;
        this.placeMapper = placeMapper;
        this.placeRepository = placeRepository;
        this.userMapper = userMapper;
    }

    public List<EventDto> searchEventsByOrganizerNameOrPlaceName(String organizerName, String placeName) throws EventNotFoundException {
        List<EventEntity> events = additionalRepository.findByOrganizerOrPlace(organizerName, placeName);
        if (events.isEmpty()) {
            throw new EventNotFoundException("No events found with given search parameters");
        }
        return eventMapper.toDtoList(events);
    }

    public List<String> searchPlacesByOrganizerName(String organizerName) throws OrganizerNotFoundException {
        List<String> places = additionalRepository.findPlacesByOrganizerName(organizerName);
        if (places.isEmpty()) {
            throw new OrganizerNotFoundException("No places found for the given organizer name");
        }
        return places;
    }

    public Integer getEventsCountByOrganizerName(String name) throws OrganizerNotFoundException {
        OrganizerEntity organizer = organizerRepository.findByName(name)
                .orElseThrow(() -> new OrganizerNotFoundException("Organizer with name " + name + " not found"));
        return additionalRepository.countByOrganizer(organizer);
    }

    public long getEventsCountByPlaceName(String placeName) throws PlaceNotFoundException {
        List<EventEntity> events = additionalRepository.findByPlaceName(placeName);
        if (events.isEmpty()) {
            throw new PlaceNotFoundException("No events found for the given place name");
        }
        return events.size();
    }

    public List<UserDto> getUsersByAddressId(Long addressId) {
        List<UserEntity> users = additionalRepository.findUsersByAddressId(addressId);
        if (users.isEmpty()) {
            throw new EntityNotFoundException("No users found for address with id " + addressId);
        }
        return users.stream()
                .map(user -> new UserDto(user.getId(), user.getUserName(), user.getUserSurname()))
                .collect(Collectors.toList());
    }

    public AddressDto getAddressById(Long addressId) {
        AddressEntity address = additionalRepository.findAddressById(addressId);
        return new AddressDto(address.getId(), address.getStreet(), address.getCity(), address.getCountry());
    }

    public List<UserDto> getUsersByUserAddress() {
        List<UserEntity> users = additionalRepository.findUsersByUserAddress();
        return users.stream()
                .map(user -> new UserDto(user.getId(), user.getUserName(), user.getUserSurname()))
                .collect(Collectors.toList());
    }

    public List<AddressUsersDto> getAddressUsers() {
        List<Object[]> addressUserTuples = additionalRepository.findAddressUsers();
        if (addressUserTuples.isEmpty()) {
            throw new EntityNotFoundException("No users found.");
        }

        Map<Long, List<UserDto>> addressUsersMap = new HashMap<>();
        for (Object[] tuple : addressUserTuples) {
            UserEntity user = (UserEntity) tuple[0];
            AddressEntity address = (AddressEntity) tuple[1];

            UserDto userDto = new UserDto(user.getId(), user.getUserName(), user.getUserSurname());
            if (addressUsersMap.containsKey(address.getId())) {
                addressUsersMap.get(address.getId()).add(userDto);
            } else {
                List<UserDto> users = new ArrayList<>();
                users.add(userDto);
                addressUsersMap.put(address.getId(), users);
            }
        }

        List<AddressUsersDto> addressUsers = new ArrayList<>();
        for (Map.Entry<Long, List<UserDto>> entry : addressUsersMap.entrySet()) {
            Long addressId = entry.getKey();
            AddressEntity address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new EntityNotFoundException("Address not found."));
            List<UserDto> users = entry.getValue();
            int userCount = users.size();
            AddressUsersDto addressUsersDto = new AddressUsersDto(addressId, address.getStreet(), address.getCity(),
                    address.getPostCode(), userCount, users);
            addressUsers.add(addressUsersDto);
        }
        return addressUsers;
    }



}

