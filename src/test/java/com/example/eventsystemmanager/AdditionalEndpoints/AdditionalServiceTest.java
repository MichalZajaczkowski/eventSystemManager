package com.example.eventsystemmanager.AdditionalEndpoints;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdditionalServiceTest {

    @Mock
    private AdditionalRepository additionalRepository;

    @Mock
    private OrganizerRepository organizerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private PlaceMapper placeMapper;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AdditionalService additionalService;

    private List<String> places;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        additionalService = new AdditionalService(additionalRepository, organizerRepository, addressRepository,
                eventMapper, placeMapper, placeRepository, userMapper);
    }

    @Test
    @DisplayName("Search events by organizer name or place name - Success")
    public void testSearchEventsByOrganizerNameOrPlaceNameSuccess() throws EventNotFoundException {
        EventEntity event1 = new EventEntity();
        EventEntity event2 = new EventEntity();
        List<EventEntity> events = Arrays.asList(event1, event2);
        when(additionalRepository.findByOrganizerOrPlace("Organizer1", "Place1")).thenReturn(events);

        EventDto eventDto1 = new EventDto();
        EventDto eventDto2 = new EventDto();
        when(eventMapper.toDtoList(events)).thenReturn(Arrays.asList(eventDto1, eventDto2));

        List<EventDto> result = additionalService.searchEventsByOrganizerNameOrPlaceName("Organizer1", "Place1");
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should throw an EntityNotFoundException when no users are found")
    void getAddressUsersThrowsEntityNotFoundExceptionWhenNoUsersFound() {
        when(additionalRepository.findAddressUsers()).thenReturn(Collections.emptyList());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> additionalService.getAddressUsers());

        // Verify
        verify(additionalRepository, times(1)).findAddressUsers();
    }

    @Test
    public void searchEventsByOrganizerNameOrPlaceName_EventsFound_ShouldReturnListOfEventDto() {
        // given
        String organizerName = "Organizer1";
        String placeName = "Place1";
        List<EventEntity> events = new ArrayList<>();
        EventEntity event1 = new EventEntity();
        event1.setId(1L);
        event1.setName("Event1");
        event1.setDescription("Description1");
        events.add(event1);
        EventEntity event2 = new EventEntity();
        event2.setId(2L);
        event2.setName("Event2");
        event2.setDescription("Description2");
        events.add(event2);
        List<EventDto> expected = new ArrayList<>();
        EventDto eventDto1 = new EventDto();
        eventDto1.setId(1L);
        eventDto1.setName("Event1");
        eventDto1.setDescription("Description1");
        expected.add(eventDto1);
        EventDto eventDto2 = new EventDto();
        eventDto2.setId(2L);
        eventDto2.setName("Event2");
        eventDto2.setDescription("Description2");
        expected.add(eventDto2);
        when(additionalRepository.findByOrganizerOrPlace(organizerName, placeName)).thenReturn(events);
        when(eventMapper.toDtoList(events)).thenReturn(expected);

        // when
        List<EventDto> actual = null;
        try {
            actual = additionalService.searchEventsByOrganizerNameOrPlaceName(organizerName, placeName);
        } catch (EventNotFoundException e) {
            throw new RuntimeException(e);
        }

        // then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void searchEventsByOrganizerNameOrPlaceName_NoEventsFound_ShouldThrowException() {
        // given
        String organizerName = "Organizer1";
        String placeName = "Place1";
        List<EventEntity> events = new ArrayList<>();
        when(additionalRepository.findByOrganizerOrPlace(organizerName, placeName)).thenReturn(events);

        // when, then
        Assertions.assertThrows(EventNotFoundException.class,
                () -> additionalService.searchEventsByOrganizerNameOrPlaceName(organizerName, placeName));
    }

    @BeforeEach
    public void setUp() {
        places = new ArrayList<>(Arrays.asList("Place 1", "Place 2", "Place 3"));
    }

    @Test
    public void testSearchPlacesByOrganizerName() {
        String organizerName = "Organizer";
        when(additionalRepository.findPlacesByOrganizerName(organizerName)).thenReturn(places);

        List<String> result = additionalService.searchPlacesByOrganizerName(organizerName);

        assertEquals(places, result);
    }

    @Test
    public void testSearchPlacesByOrganizerNameNoPlacesFound() {
        String organizerName = "Organizer";
        when(additionalRepository.findPlacesByOrganizerName(organizerName)).thenReturn(new ArrayList<>());

        OrganizerNotFoundException thrown = assertThrows(OrganizerNotFoundException.class, () -> {
            additionalService.searchPlacesByOrganizerName(organizerName);
        });

        assertEquals("No places found for the given organizer name", thrown.getMessage());
    }

    @Test
    public void testGetEventsCountByOrganizerName() throws OrganizerNotFoundException {
        String organizerName = "Test Organizer";
        OrganizerEntity organizerEntity = new OrganizerEntity();
        organizerEntity.setName(organizerName);

        when(organizerRepository.findByName(organizerName)).thenReturn(Optional.of(organizerEntity));
        when(additionalRepository.countByOrganizer(organizerEntity)).thenReturn(5);

        Integer count = additionalService.getEventsCountByOrganizerName(organizerName);

        assertEquals(5, count);
    }

    @Test
    public void testGetEventsCountByOrganizerNameNotFound() {
        String organizerName = "Non-existent Organizer";
        when(organizerRepository.findByName(organizerName)).thenReturn(Optional.empty());

        OrganizerNotFoundException exception = assertThrows(OrganizerNotFoundException.class,
                () -> additionalService.getEventsCountByOrganizerName(organizerName));

        assertEquals("Organizer with name Non-existent Organizer not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should return the number of events for the given place name")
    void shouldReturnEventsCountByPlaceName() {
        String placeName = "Test Place";
        List<EventEntity> events = new ArrayList<>();
        events.add(new EventEntity());
        events.add(new EventEntity());
        Mockito.when(additionalRepository.findByPlaceName(placeName)).thenReturn(events);
        long expectedCount = 2;
        try {
            long actualCount = additionalService.getEventsCountByPlaceName(placeName);
            assertEquals(expectedCount, actualCount);
        } catch (PlaceNotFoundException e) {
            fail("Unexpected PlaceNotFoundException: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should throw PlaceNotFoundException when no events are found for the given place name")
    void shouldThrowPlaceNotFoundException() {
        String placeName = "Test Place";
        List<EventEntity> events = new ArrayList<>();
        Mockito.when(additionalRepository.findByPlaceName(placeName)).thenReturn(events);
        assertThrows(PlaceNotFoundException.class, () -> {
            additionalService.getEventsCountByPlaceName(placeName);
        });
    }

    @Test
    void getUsersByAddressId_withInvalidAddressId_shouldThrowEntityNotFoundException() {
        // given
        Long addressId = 1L;
        List<UserEntity> users = new ArrayList<>();

        when(additionalRepository.findUsersByAddressId(anyLong())).thenReturn(users);

        // when, then
        Assertions.assertThrows(EntityNotFoundException.class, () -> additionalService.getUsersByAddressId(addressId));
    }

    @Test
    @DisplayName("Test getAddressUsers with no data")
    public void testGetAddressUsersWithNoData() {
        when(additionalRepository.findAddressUsers()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> additionalService.getAddressUsers());
    }
}
