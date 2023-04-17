package com.example.eventsystemmanager.repository;

import com.example.eventsystemmanager.entity.UserStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<UserStatusEntity, Long> {

    Optional<UserStatusEntity> findByName(String name);

    UserStatusEntity findByValue(Integer value);
}
