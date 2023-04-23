package com.example.eventsystemmanager.place;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
