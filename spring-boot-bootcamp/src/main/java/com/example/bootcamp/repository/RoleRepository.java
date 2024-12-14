package com.example.bootcamp.repository;

import com.example.bootcamp.entity.ERole;
import com.example.bootcamp.entity.Role;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
