package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.category.Category;
import com.example.eventsystemmanager.event.eventStatus.EventStatus;
import com.example.eventsystemmanager.organizer.OrganizerEntity;
import com.example.eventsystemmanager.place.PlaceEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private PlaceEntity place;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "organizer_id")
    private OrganizerEntity organizer;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "event_start_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventStartDate;

    @Column(name = "event_end_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventEndDate;

    @Column(name = "date_of_creat")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @Column(name = "date_of_modification")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;
}
// TODO: 02.05.2023 rozbicie dat na daty i czas osobno
