package com.example.bootcamp.controller;

import com.example.bootcamp.api.UsersApi;
import com.example.bootcamp.dto.UserReq;
import com.example.bootcamp.usecase.UserUseCase;
import com.example.bootcamp.utility.RoleConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
// @PreAuthorize(RoleConstants.USER_CRUD)
public class UserV1Controller implements UsersApi {
  private final UserUseCase userUseCase;

  @Override
  @PreAuthorize(RoleConstants.ADMIN_CRUD)
  public ResponseEntity<UserReq> getUserById(String userId) {
    UserReq userProfile = userUseCase.getUserById(userId);
    return ResponseEntity.ok(userProfile);
  }

  @Override
  @PreAuthorize(RoleConstants.USER_CRUD)
  public ResponseEntity<UserReq> getUserDetails() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();

    UserReq userProfile = userUseCase.getUser(username);

    return ResponseEntity.ok(userProfile);
  }

  @Override
  @PreAuthorize(RoleConstants.ADMIN_CRUD)
  public ResponseEntity<Object> searchUsers(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    Page<UserReq> userEntityList = userUseCase.searchUser(size, page, sortDir, query, sortBy);
    return ResponseEntity.status(HttpStatus.CREATED).body(userEntityList);
  }

  @Override
  @PreAuthorize(RoleConstants.ADMIN_CRUD)
  public ResponseEntity<UserReq> updateUser(@RequestBody UserReq userEntity) {
    UserReq updatedUserEntity = userUseCase.updateUser(userEntity);
    if (updatedUserEntity != null) {
      return ResponseEntity.ok(updatedUserEntity);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Override
  @PreAuthorize(RoleConstants.ADMIN_CRUD)
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userUseCase.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  /* @Override
  @PreAuthorize(RoleConstants.ADMIN_CRUD)
  public ResponseEntity<List<UserReq>> getUsers() {
    List<UserReq> userEntityList = userUseCase.getUsers();

    return ResponseEntity.status(HttpStatus.OK).body(userEntityList);
  }*/

  @Override
  public ResponseEntity<UserReq> updateUserProfilePhoto(MultipartFile file) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    UserReq updatedUserEntity = userUseCase.uploadUserProfile(file, username);

    return ResponseEntity.ok(updatedUserEntity);
  }
}
