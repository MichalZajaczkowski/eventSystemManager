package com.example.eventsystemmanager.repository;

import com.example.eventsystemmanager.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Long> {

    Optional<StatusEntity> findByName(String name);
}
