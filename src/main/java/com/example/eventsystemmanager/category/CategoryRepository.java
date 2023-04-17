package com.example.eventsystemmanager.category;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findAll();

    Optional<CategoryEntity> findById(Long id);

    CategoryEntity save(CategoryEntity category);

    void deleteById(Long id);

    void deleteAll();

    Optional<Object> findByDescription(String description);
    Optional<Object> findById(SingularAttribute<AbstractPersistable, Serializable> id);

}
