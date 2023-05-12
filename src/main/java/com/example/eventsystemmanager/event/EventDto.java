package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.category.Category;
import com.example.eventsystemmanager.event.eventStatus.EventStatus;
import com.example.eventsystemmanager.organizer.OrganizerDto;
import com.example.eventsystemmanager.organizer.OrganizerEntity;
import com.example.eventsystemmanager.place.PlaceDto;
import com.example.eventsystemmanager.place.PlaceEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDto {

    private Long id;

    @NotNull(message = "Place cannot be null")
    private PlaceDto place;

    private OrganizerDto organizer;

    @NotNull(message = "Category cannot be null")
    @Enumerated(EnumType.STRING)
    private Category category;


    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be in the present or future")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventStartDate;

    @NotNull(message = "End date cannot be null")
    @FutureOrPresent(message = "End date must be in the present or future")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventEndDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    public void setPlaceToDto (PlaceEntity placeEntity) {
        this.place = new PlaceDto(placeEntity);
    }

    public void setOrganizerToDto(OrganizerEntity organizerEntity) {
        this.organizer = new OrganizerDto(organizerEntity);
    }
}
