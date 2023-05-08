package com.example.eventsystemmanager.AdditionalEndpoints;

import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.event.EventEntity;
import com.example.eventsystemmanager.organizer.OrganizerEntity;
import com.example.eventsystemmanager.place.PlaceEntity;
import com.example.eventsystemmanager.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AdditionalRepository extends JpaRepository<EventEntity, Long> {

    @Query("SELECT e FROM EventEntity e JOIN e.place p JOIN e.organizer o WHERE p.name LIKE %:placeName% OR o.name LIKE %:organizerName%")
    List<EventEntity> findByOrganizerOrPlace(@Param("organizerName") String organizerName, @Param("placeName") String placeName);

    @Query("SELECT DISTINCT e.place FROM EventEntity e WHERE e.organizer.id = :organizerId")
    List<PlaceEntity> findPlacesByOrganizerId(@Param("organizerId") Long organizerId);


    @Query("SELECT DISTINCT p.name FROM EventEntity e JOIN e.place p JOIN e.organizer o WHERE o.name = :organizerName")
    List<String> findPlacesByOrganizerName(@Param("organizerName") String organizerName);

    @Query("SELECT COUNT(e) FROM EventEntity e WHERE e.organizer = :organizer")
    Integer countByOrganizer(@Param("organizer") OrganizerEntity organizer);

    @Query("SELECT e FROM EventEntity e JOIN e.place p WHERE p.name = :placeName")
    List<EventEntity> findByPlaceName(@Param("placeName") String placeName);

    @Query("SELECT u FROM UserEntity u WHERE u.addressEntity.id = :addressId")
    List<UserEntity> findUsersByAddressId(Long addressId);

    @Query("SELECT a FROM AddressEntity a WHERE a.id = :addressId")
    AddressEntity findAddressById(Long addressId);

    @Query("SELECT u FROM UserEntity u WHERE u.addressEntity.addressType = 'USER_ADDRESS'")
    List<UserEntity> findUsersByUserAddress();

    @Query("SELECT u, a FROM UserEntity u JOIN u.addressEntity a WHERE a.id IN (SELECT ua.addressEntity.id FROM UserEntity ua GROUP BY ua.addressEntity.id HAVING COUNT(DISTINCT ua.id) > 1)")
    List<Object[]> findAddressUsers();



}
