package com.example.hibernatebootcamp.repository;

import com.example.hibernatebootcamp.entity.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Blog entity.
 */
@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {


    List<BlogEntity> findByTitleContainingIgnoreCase(String title);

    List<BlogEntity> findByAuthorId(Long authorId);

    List<BlogEntity> findByCategoriesId(Long categoryId);
} 