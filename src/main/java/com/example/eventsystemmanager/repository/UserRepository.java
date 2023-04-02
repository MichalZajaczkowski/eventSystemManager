package com.example.eventsystemmanager.repository;

import com.example.eventsystemmanager.entity.UserEntity;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAll();

    Optional<UserEntity> findById(Long id);

    UserEntity save(UserEntity userEntity);

    void deleteById(Long id);

    void deleteAll();

    Optional<Object> findById(SingularAttribute<AbstractPersistable, Serializable> id);
}
