package com.example.hibernatebootcamp.repository;

import com.example.hibernatebootcamp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
} 