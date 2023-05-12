package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.category.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    @DisplayName("Should return BAD_REQUEST status when input is invalid")
    void createEventReturnsBadRequestWhenInputIsInvalid() {
        EventDto eventDto =
                EventDto.builder()
                        .id(1L)
                        .place(null)
                        .organizer(null)
                        .category(Category.MUSIC)
                        .name("Test Event")
                        .description("This is a test event")
                        .eventStartDate(LocalDateTime.now())
                        .eventEndDate(LocalDateTime.now().plusHours(2))
                        .build();

        ResponseEntity<EventDto> responseEntity = eventController.createEvent(eventDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        verify(eventService, never()).createEvent(any(EventDto.class));
    }

    @Test
    @DisplayName("Should create an event and return CREATED status")
    void createEventReturnsCreatedStatus() {
        EventDto eventDto =
                EventDto.builder()
                        .id(1L)
                        .name("Test Event")
                        .description("Test Event Description")
                        .category(Category.MUSIC)
                        .eventStartDate(LocalDateTime.now())
                        .eventEndDate(LocalDateTime.now().plusDays(1))
                        .build();

        when(eventService.createEvent(eventDto)).thenReturn(eventDto);

        ResponseEntity<EventDto> responseEntity = eventController.createEvent(eventDto);

        verify(eventService, times(1)).createEvent(eventDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(eventDto);
    }

    @Test
    @DisplayName("Should create an event and return the created event with status CREATED")
    void createEventReturnsCreatedEventWithStatusCreated() {
        EventDto eventDto = new EventDto();
        when(eventService.createEvent(eventDto)).thenReturn(eventDto);

        ResponseEntity<EventDto> response = eventController.createEvent(eventDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(eventDto, response.getBody());
        verify(eventService, times(1)).createEvent(eventDto);
    }
}