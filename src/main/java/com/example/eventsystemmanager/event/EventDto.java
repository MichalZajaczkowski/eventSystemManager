package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.category.CategoryDto;
import com.example.eventsystemmanager.event.eventStatus.EventStatus;
import com.example.eventsystemmanager.organizer.OrganizerDto;
import com.example.eventsystemmanager.place.PlaceDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventDto {

    private Long id;

    @NotNull(message = "Place cannot be null")
    private PlaceDto place;

    @NotNull(message = "Organizer cannot be null")
    private OrganizerDto organizer;

    @NotNull(message = "Category cannot be null")
    private CategoryDto category;

    @NotNull(message = "Event status cannot be null")
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

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;
}
