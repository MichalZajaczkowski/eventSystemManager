package com.example.eventsystemmanager.user;

import com.example.eventsystemmanager.user.userStatus.UserStatus;
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
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAll();

    Optional<UserEntity> findById(Long id);
    @Query("SELECT u FROM UserEntity u WHERE u.userStatus = :userStatus")
    List<UserEntity> findByUserStatus(@Param("userStatus") UserStatus userStatus);

    UserEntity save(UserEntity userEntity);

    void deleteById(Long id);

    void deleteAll();

    Optional<Object> findById(SingularAttribute<AbstractPersistable, Serializable> id);
}
