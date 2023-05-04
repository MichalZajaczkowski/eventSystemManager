package com.example.eventsystemmanager.event;

import com.example.eventsystemmanager.place.PlaceEntity;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    List<EventEntity> findAll();

    Optional<EventEntity> findById(Long id);

    EventEntity save(EventEntity eventEntity);

    @Override
    void deleteById(Long id);

    @Override
    void deleteAll();

    Optional<Object> findById(SingularAttribute<AbstractPersistable, Serializable> id);
}
