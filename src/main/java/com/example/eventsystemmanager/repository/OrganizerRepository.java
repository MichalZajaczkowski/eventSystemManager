package com.example.eventsystemmanager.repository;

import com.example.eventsystemmanager.entity.OrganizerEntity;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizerRepository extends JpaRepository<OrganizerEntity, Long> {

    List<OrganizerEntity> findAll();
    Optional<OrganizerEntity> findById(Long id);
    OrganizerEntity save(OrganizerEntity organizer);
    void deleteById(Long id);
    void deleteAll();
    @Query("SELECT u FROM OrganizerEntity u WHERE u.name = :name")
    Optional<OrganizerEntity> findByName(String name);
    Optional<Object> findById(SingularAttribute<AbstractPersistable, Serializable> id);
}
