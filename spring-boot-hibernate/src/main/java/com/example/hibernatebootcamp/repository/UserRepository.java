package com.example.hibernatebootcamp.repository;

import com.example.hibernatebootcamp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
} 