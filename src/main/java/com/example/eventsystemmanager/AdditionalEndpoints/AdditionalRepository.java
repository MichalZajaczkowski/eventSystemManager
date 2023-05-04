package com.example.eventsystemmanager.AdditionalEndpoints;

import com.example.eventsystemmanager.event.EventEntity;
import com.example.eventsystemmanager.place.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdditionalRepository extends JpaRepository<EventEntity, Long> {

    @Query("SELECT e FROM EventEntity e JOIN e.place p JOIN e.organizer o WHERE p.name LIKE %:placeName% OR o.name LIKE %:organizerName%")
    List<EventEntity> findByOrganizerOrPlace(@Param("organizerName") String organizerName, @Param("placeName") String placeName);

    @Query("SELECT DISTINCT e.place FROM EventEntity e WHERE e.organizer.id = :organizerId")
    List<PlaceEntity> findPlacesByOrganizerId(@Param("organizerId") Long organizerId);

}
