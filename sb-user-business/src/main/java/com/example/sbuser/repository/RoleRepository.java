package com.example.sbuser.repository;

import com.example.sbuser.entity.ERole;
import com.example.sbuser.entity.Role;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
