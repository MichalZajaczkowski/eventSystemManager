package com.example.eventsystemmanager.repository;

import com.example.eventsystemmanager.entity.UserAddress;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress,Long> {

    List<UserAddress> findAll();

    Optional<UserAddress> findById(Long id);

    UserAddress save(UserAddress userAddress);

    void deleteById(Long id);

    void deleteAll();

    Optional<Object> findById(SingularAttribute<AbstractPersistable, Serializable> id);

}
