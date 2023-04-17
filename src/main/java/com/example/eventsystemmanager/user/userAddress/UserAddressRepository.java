package com.example.eventsystemmanager.user.userAddress;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserAddressRepository extends JpaRepository<UserAddressEntity,Long> {

    List<UserAddressEntity> findAll();

    Optional<UserAddressEntity> findById(Long id);

    UserAddressEntity save(UserAddressEntity userAddressEntity);

    void deleteById(Long id);

    void deleteAll();

    Optional<Object> findById(SingularAttribute<AbstractPersistable, Serializable> id);

}
