package com.example.hibernatebootcamp.service;

import com.example.hibernatebootcamp.entity.UserEntity;
import com.example.hibernatebootcamp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity saveUser(UserEntity userEntity) {
        if (userEntity == null) {
            throw new IllegalArgumentException("User entity cannot be null.");
        }
        if (userEntity.getUsername() == null || userEntity.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (userEntity.getEmail() == null || userEntity.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }
        return userRepository.save(userEntity);
    }
} 