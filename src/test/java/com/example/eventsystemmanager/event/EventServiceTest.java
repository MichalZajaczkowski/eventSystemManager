package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.event.eventStatus.EventStatus;
import com.example.eventsystemmanager.exception.EventNotFoundException;
import com.example.eventsystemmanager.exception.EventSaveException;
import com.example.eventsystemmanager.exception.OrganizerNotFoundException;
import com.example.eventsystemmanager.organizer.OrganizerDto;
import com.example.eventsystemmanager.organizer.OrganizerEntity;
import com.example.eventsystemmanager.organizer.OrganizerRepository;
import com.example.eventsystemmanager.organizer.OrganizerService;
import com.example.eventsystemmanager.place.PlaceDto;
import com.example.eventsystemmanager.place.PlaceEntity;
import com.example.eventsystemmanager.place.PlaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;
    @Mock
    private OrganizerService organizerService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private EventMapper eventMapper;
    @Mock
    private OrganizerRepository organizerRepository;

    @Test
    @DisplayName("Test findAllEvents - events exist")
    public void testFindAllEvents_EventsExist() {
        // given
        PlaceEntity place1 = new PlaceEntity();
        place1.setId(1L);
        OrganizerEntity organizer1 = new OrganizerEntity();
        organizer1.setId(1L);
        EventEntity event1 = new EventEntity();
        event1.setPlace(place1);
        event1.setOrganizer(organizer1);

        PlaceEntity place2 = new PlaceEntity();
        place2.setId(2L);
        OrganizerEntity organizer2 = new OrganizerEntity();
        organizer2.setId(2L);
        EventEntity event2 = new EventEntity();
        event2.setPlace(place2);
        event2.setOrganizer(organizer2);

        List<EventEntity> expectedEvents = Arrays.asList(event1, event2);
        when(eventRepository.findAll()).thenReturn(expectedEvents);

        // when
        List<EventDto> actualEvents = eventService.findAllEvents();

        // then
        assertNotNull(actualEvents);
        assertEquals(expectedEvents.size(), actualEvents.size());

        for (int i = 0; i < expectedEvents.size(); i++) {
            EventEntity expectedEntity = expectedEvents.get(i);
            EventDto actualDto = actualEvents.get(i);

            // Compare the properties of expectedEntity and actualDto
            assertEquals(expectedEntity.getId(), actualDto.getId());
            assertEquals(expectedEntity.getName(), actualDto.getName());
            // and so on for all properties...
        }
    }


    @Test
    @DisplayName("Test findAllEvents - no events")
    public void testFindAllEvents_NoEvents() {
        // Arrange
        List<EventEntity> expectedEvents = Arrays.asList();

        when(eventRepository.findAll()).thenReturn(expectedEvents);

        // Act
        List<EventDto> actualEvents = eventService.findAllEvents();

        // Assert
        assertEquals(expectedEvents, actualEvents);
    }

    @Test
    @DisplayName("Test findById - event exists")
    public void testFindById_EventExists() throws EventNotFoundException {
        // given
        PlaceEntity place1 = new PlaceEntity();
        place1.setId(1L);
        OrganizerEntity organizer1 = new OrganizerEntity();
        organizer1.setId(1L);
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(1L);
        eventEntity.setPlace(place1);
        eventEntity.setOrganizer(organizer1);
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
//        when(modelMapper.map(eventEntity, EventDto.class)).thenReturn(eventDto);
        lenient().when(modelMapper.map(eventEntity, EventDto.class)).thenReturn(eventDto);

        // when
        Optional<EventDto> actualEventDto = Optional.ofNullable(eventService.findById(1L));

        // then
        assertTrue(actualEventDto.isPresent());
        assertEquals(eventDto.getId(), actualEventDto.get().getId());
    }

    @Test
    @DisplayName("Test findById with invalid ID")
    public void testFindByIdWithInvalidId() {
        // Arrange
        Long id = 1L;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EventNotFoundException.class, () -> eventService.findById(id));
    }

    @Test
    @DisplayName("Test dla poprawnego utworzenia eventu")
    void createEvent_Success() {
        // given
        EventDto eventDto = new EventDto();
        eventDto.setName("Test Event");
        eventDto.setDescription("Test Description");

        EventEntity eventEntity = new EventEntity();
        eventEntity.setName("Test Event");
        eventEntity.setDescription("Test Description");

        when(eventMapper.toEntity(eventDto)).thenReturn(eventEntity);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);
        when(eventMapper.toDto(eventEntity)).thenReturn(eventDto);

        // when
        EventDto createdEvent = eventService.createEvent(eventDto);

        // then
        assertNotNull(createdEvent);
        assertEquals(eventDto.getName(), createdEvent.getName());
        assertEquals(eventDto.getDescription(), createdEvent.getDescription());
    }

    @Test
    @DisplayName("Test dla próby utworzenia eventu z nieudanym zapisem")
    void createEvent_FailToSave() {
        // given
        EventDto eventDto = new EventDto();
        eventDto.setName("Test Event");
        eventDto.setDescription("Test Description");

        EventEntity eventEntity = new EventEntity();
        eventEntity.setName("Test Event");
        eventEntity.setDescription("Test Description");

        when(eventMapper.toEntity(eventDto)).thenReturn(eventEntity);
        when(eventRepository.save(any(EventEntity.class))).thenThrow(new RuntimeException());

        // when / then
        assertThrows(EventSaveException.class, () -> eventService.createEvent(eventDto));
    }

    @Test
    @DisplayName("Test dla próby utworzenia eventu z nieistniejącym organizatorem")
    void createEvent_OrganizerNotFound() {
        // given
        EventDto eventDto = new EventDto();
        eventDto.setName("Test Event");
        eventDto.setDescription("Test Description");

        OrganizerDto organizerDto = new OrganizerDto();
        organizerDto.setName("Test Organizer");
        eventDto.setOrganizer(organizerDto);

        EventEntity eventEntity = new EventEntity();
        eventEntity.setName("Test Event");
        eventEntity.setDescription("Test Description");

        when(eventMapper.toEntity(eventDto)).thenReturn(eventEntity);
        when(organizerRepository.findOrgByName(organizerDto.getName())).thenReturn(null);
        when(organizerService.createOrganizer(organizerDto)).thenThrow(new OrganizerNotFoundException("nie pykło"));

        // when / then
        assertThrows(OrganizerNotFoundException.class, () -> eventService.createEvent(eventDto));
    }

    @Test
    @DisplayName("Test rzucania wyjątku, gdy nie można znaleźć organizatora o podanym ID")
    void createEventByOrganizer_OrganizerIdNotFound() {
        // given
        EventDto eventDto = new EventDto();
        Long organizerId = 1L;

        when(organizerRepository.findById(organizerId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(OrganizerNotFoundException.class, () -> eventService.createEventByOrganizer(eventDto, organizerId, null));
    }

    @Test
    @DisplayName("Test rzucania wyjątku, gdy nie można znaleźć organizatora o podanej nazwie")
    void createEventByOrganizer_OrganizerNameNotFound() {
        // given
        EventDto eventDto = new EventDto();
        String organizerName = "Test Organizer";

        when(organizerRepository.findByName(organizerName)).thenReturn(Optional.empty());

        // when / then
        assertThrows(OrganizerNotFoundException.class, () -> eventService.createEventByOrganizer(eventDto, null, organizerName));
    }

    @Test
    @DisplayName("Test poprawnego zapisywania wydarzenia, gdy organizator jest znaleziony")
    void createEventByOrganizer_OrganizerFound() {
        // given
        EventDto eventDto = new EventDto();
        Long organizerId = 1L;
        String organizerName = "Test Organizer";
        OrganizerEntity organizerEntity = new OrganizerEntity();
        organizerEntity.setId(organizerId);
        organizerEntity.setName(organizerName);

        EventEntity eventEntity = new EventEntity();
        EventEntity savedEventEntity = new EventEntity();

        when(eventMapper.toEntity(eventDto)).thenReturn(eventEntity);
        when(organizerRepository.findById(organizerId)).thenReturn(Optional.of(organizerEntity));
        when(eventRepository.save(eventEntity)).thenReturn(savedEventEntity);
        when(eventMapper.toDto(savedEventEntity)).thenReturn(eventDto);

        // when
        EventDto result = eventService.createEventByOrganizer(eventDto, organizerId, null);

        // then
        assertEquals(eventDto, result);
        verify(eventRepository).save(eventEntity);
    }

    @Test
    @DisplayName("Test rzucania wyjątku, gdy nie można znaleźć wydarzenia o podanym ID")
    void partialUpdateEventsData_EventIdNotFound() {
        // given
        Long eventId = 1L;
        EventDto eventDto = new EventDto();

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(IllegalStateException.class, () -> eventService.partialUpdateEventsData(eventId, eventDto));
    }

    @Test
    @DisplayName("Test poprawnej aktualizacji danych wydarzenia")
    void partialUpdateEventsData_SuccessfulUpdate() {
        // given
        Long eventId = 1L;
        EventDto eventDto = new EventDto();
        eventDto.setName("Updated Event");
        EventEntity eventToUpdate = new EventEntity();
        eventToUpdate.setName("Old Event");

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventToUpdate));
        when(eventRepository.save(eventToUpdate)).thenReturn(eventToUpdate);

        // when
        EventDto result = eventService.partialUpdateEventsData(eventId, eventDto);

        // then
        assertEquals(eventDto.getName(), result.getName());
        verify(eventRepository).save(eventToUpdate);
    }

    @Test
    @DisplayName("Test ustawiania statusu wydarzenia na POSTPONED_UPCOMING, gdy podana jest nowa data rozpoczęcia lub zakończenia")
    void partialUpdateEventsData_NewDate_StatusPostponedUpcoming() {
        // given
        Long eventId = 1L;
        EventDto eventDto = new EventDto();
        eventDto.setEventStartDate(LocalDateTime.now().plusDays(1));
        EventEntity eventToUpdate = new EventEntity();
        eventToUpdate.setEventStatus(EventStatus.UPCOMING);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventToUpdate));
        when(eventRepository.save(eventToUpdate)).thenReturn(eventToUpdate);

        // when
        EventDto result = eventService.partialUpdateEventsData(eventId, eventDto);

        // then
        assertEquals(EventStatus.POSTPONED_UPCOMING, eventToUpdate.getEventStatus());
        verify(eventRepository).save(eventToUpdate);
    }

    @Test
    @DisplayName("Test poprawnej aktualizacji miejsca wydarzenia")
    void updatePlaceForEvent_SuccessfulUpdate() {
        // given
        Long eventId = 1L;
        PlaceDto placeDto = new PlaceDto();
        placeDto.setName("Updated Place");
        EventEntity eventToUpdate = new EventEntity();
        PlaceEntity oldPlace = new PlaceEntity();
        PlaceEntity newPlace = new PlaceEntity();
        newPlace.setName("Updated Place");

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventToUpdate));
        when(placeRepository.findByName(placeDto.getName())).thenReturn(newPlace);
        when(eventRepository.save(eventToUpdate)).thenReturn(eventToUpdate);

        // when
        EventDto result = eventService.updatePlaceForEvent(eventId, placeDto);

        // then
        assertEquals(newPlace, eventToUpdate.getPlace());
        verify(eventRepository).save(eventToUpdate);
    }

    @Test
    @DisplayName("Test ustawiania statusu wydarzenia na POSTPONED_UPCOMING, gdy miejsce wydarzenia jest aktualizowane")
    void updatePlaceForEvent_NewPlace_StatusPostponedUpcoming() {
        // given
        Long eventId = 1L;
        PlaceDto placeDto = new PlaceDto();
        placeDto.setName("Updated Place");
        EventEntity eventToUpdate = new EventEntity();
        PlaceEntity oldPlace = new PlaceEntity();
        PlaceEntity newPlace = new PlaceEntity();
        newPlace.setName("Updated Place");
        eventToUpdate.setEventStatus(EventStatus.UPCOMING);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventToUpdate));
        when(placeRepository.findByName(placeDto.getName())).thenReturn(newPlace);
        when(eventRepository.save(eventToUpdate)).thenReturn(eventToUpdate);

        // when
        EventDto result = eventService.updatePlaceForEvent(eventId, placeDto);

        // then
        assertEquals(EventStatus.POSTPONED_UPCOMING, eventToUpdate.getEventStatus());
        verify(eventRepository).save(eventToUpdate);
    }

    @Test
    @DisplayName("Test aktualizacji statusu na FINISHED po zakończeniu wydarzenia")
    void updateSingleEventStatus_WhenEventHasEnded() {
        // given
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(1L);
        eventEntity.setEventStartDate(LocalDateTime.now().minusDays(2));
        eventEntity.setEventEndDate(LocalDateTime.now().minusDays(1));
        eventEntity.setEventStatus(EventStatus.IN_PROGRESS);

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));

        // when
        eventService.updateSingleEventStatus(eventEntity.getId());

        // then
        assertEquals(EventStatus.FINISHED, eventEntity.getEventStatus());
    }

    @Test
    @DisplayName("Test aktualizacji statusu na UPCOMING przed rozpoczęciem wydarzenia")
    void updateSingleEventStatus_WhenEventHasNotStarted() {
        // given
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(1L);
        eventEntity.setEventStartDate(LocalDateTime.now().plusDays(1));
        eventEntity.setEventEndDate(LocalDateTime.now().plusDays(2));
        eventEntity.setEventStatus(EventStatus.IN_PROGRESS);

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));

        // when
        eventService.updateSingleEventStatus(eventEntity.getId());

        // then
        assertEquals(EventStatus.UPCOMING, eventEntity.getEventStatus());
    }

    @Test
    @DisplayName("Test aktualizacji statusu na IN_PROGRESS podczas trwania wydarzenia")
    void updateSingleEventStatus_WhenEventIsInProgress() {
        // given
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(1L);
        eventEntity.setEventStartDate(LocalDateTime.now().minusHours(1));
        eventEntity.setEventEndDate(LocalDateTime.now().plusHours(1));
        eventEntity.setEventStatus(EventStatus.UPCOMING);

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));

        // when
        eventService.updateSingleEventStatus(eventEntity.getId());

        // then
        assertEquals(EventStatus.IN_PROGRESS, eventEntity.getEventStatus());
    }

    @Test
    @DisplayName("Test poprawnej aktualizacji statusu wydarzenia")
    void setStatus_UpdatesStatusSuccessfully() {
        // given
        Long eventId = 1L;
        String status = "CANCELLED";
        EventEntity event = new EventEntity();
        event.setId(eventId);
        event.setEventStatus(EventStatus.UPCOMING);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // when
        eventService.setStatus(eventId, status);

        // then
        assertEquals(EventStatus.CANCELLED, event.getEventStatus());
        assertNull(event.getEventStartDate());
        assertNull(event.getEventEndDate());
        assertNull(event.getPlace());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    @DisplayName("Test rzucenia wyjątku przy nieistniejącym wydarzeniu")
    void setStatus_ThrowsExceptionWhenEventDoesNotExist() {
        // given
        Long eventId = 1L;
        String status = "CANCELLED";

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> eventService.setStatus(eventId, status));
        verify(eventRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Test rzucenia wyjątku przy niepoprawnym statusie")
    void setStatus_ThrowsExceptionWhenStatusIsInvalid() {
        // given
        Long eventId = 1L;
        String status = "INVALID_STATUS";
        EventEntity event = new EventEntity();
        event.setId(eventId);
        event.setEventStatus(EventStatus.UPCOMING);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> eventService.setStatus(eventId, status));
        verify(eventRepository, times(0)).save(any());
    }
}
