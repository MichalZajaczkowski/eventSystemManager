package com.example.eventsystemmanager.user.userAddress;

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
public interface UserAddressRepository extends JpaRepository<UserAddressEntity,Long> {

    List<UserAddressEntity> findAll();
    @Query("SELECT ua FROM UserAddressEntity ua WHERE ua.country = :country AND ua.city = :city AND ua.street = :street AND ua.buildingNumber = :buildingNumber AND ua.localNumber = :localNumber AND ua.postCode = :postCode")
    UserAddressEntity findByAddressFields(@Param("country") String country, @Param("city") String city, @Param("street") String street, @Param("buildingNumber") Integer buildingNumber, @Param("localNumber") Integer localNumber, @Param("postCode") Integer postCode);


    Optional<UserAddressEntity> findById(Long id);

    UserAddressEntity save(UserAddressEntity userAddressEntity);

    void deleteById(Long id);

    void deleteAll();

    Optional<Object> findById(SingularAttribute<AbstractPersistable, Serializable> id);

}
