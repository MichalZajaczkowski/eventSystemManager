package com.example.eventsystemmanager.place;

import com.example.eventsystemmanager.address.AddressEntity;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {

    List<PlaceEntity> findAll();

    Optional<PlaceEntity> findById(Long id);

    PlaceEntity save(PlaceEntity placeEntity);

    @Override
    void deleteById(Long id);

    @Override
    void deleteAll();

    Optional<Object> findById(SingularAttribute<AbstractPersistable, Serializable> id);

    @Query("SELECT f FROM PlaceEntity f WHERE f.placeAddressEntity = :placeAddressEntity AND f.name = :name AND f.shortName = :shortName AND f.description = :description AND f.quantityAvailablePlaces = :quantityAvailablePlaces")
    boolean findPlaceByFields(@Param("placeAddressEntity") AddressEntity placeAddressEntity, @Param("name") String name, @Param("shortName") String shortName, @Param("description") String description, @Param("quantityAvailablePlaces") Integer quantityAvailablePlaces);
}