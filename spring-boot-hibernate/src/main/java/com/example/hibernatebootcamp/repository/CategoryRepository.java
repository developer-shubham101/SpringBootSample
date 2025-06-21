package com.example.hibernatebootcamp.repository;

import com.example.hibernatebootcamp.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Category entity.
 */
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    /**
     * Finds a category by its name.
     *
     * @param name The name of the category.
     * @return An Optional containing the category if found.
     */
    Optional<CategoryEntity> findByName(String name);
} 