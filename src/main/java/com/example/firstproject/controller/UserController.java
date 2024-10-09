package com.example.firstproject.controller;

import com.example.firstproject.entity.UserEntity;
import com.example.firstproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired private UserService userService;

  @PostMapping
  public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity) {
    System.out.println("createUser");
    System.out.println(userEntity);
    UserEntity createdUserEntity = userService.createUser(userEntity);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUserEntity);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserEntity> updateUser(
      @PathVariable String id, @RequestBody UserEntity userEntity) {
    UserEntity updatedUserEntity = userService.updateUser(id, userEntity);
    if (updatedUserEntity != null) {
      return ResponseEntity.ok(updatedUserEntity);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
