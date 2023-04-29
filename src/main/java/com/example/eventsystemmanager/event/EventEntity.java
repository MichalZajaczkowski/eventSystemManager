package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.category.CategoryEntity;
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
@Entity
@Table(name = "eventss")
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

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

    @Column(name = "date of creat")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @Column(name = "date of modification")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;
}
