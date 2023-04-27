package com.example.eventsystemmanager.address;

import com.example.eventsystemmanager.address.addressType.AddressType;
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
public interface AddressRepository extends JpaRepository<AddressEntity,Long> {

    List<AddressEntity> findAll();
    @Query("SELECT ua FROM AddressEntity ua WHERE ua.country = :country AND ua.city = :city AND ua.street = :street AND ua.buildingNumber = :buildingNumber AND ua.localNumber = :localNumber AND ua.postCode = :postCode AND ua.addressType = :addressType")
    AddressEntity findByAddressFields(@Param("country") String country, @Param("city") String city, @Param("street") String street, @Param("buildingNumber") Integer buildingNumber, @Param("localNumber") Integer localNumber, @Param("postCode") Integer postCode, @Param("addressType") AddressType addressType);

    @Query("SELECT ua FROM AddressEntity ua WHERE ua.id = :id OR (ua.country = :country AND ua.city = :city AND ua.street = :street AND ua.buildingNumber = :buildingNumber AND ua.localNumber = :localNumber AND ua.postCode = :postCode AND ua.addressType = :addressType)")
    AddressEntity findByIdOrFindByAddressFields(@Param("id") Long id, @Param("country") String country, @Param("city") String city, @Param("street") String street, @Param("buildingNumber") Integer buildingNumber, @Param("localNumber") Integer localNumber, @Param("postCode") Integer postCode, @Param("addressType") AddressType addressType);


    Optional<AddressEntity> findById(Long id);

    AddressEntity save(AddressEntity addressEntity);

    void deleteById(Long id);

    void deleteAll();

    Optional<Object> findById(SingularAttribute<AbstractPersistable, Serializable> id);

}
