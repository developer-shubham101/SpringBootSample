package com.example.sbuser.controller;

import com.example.sbuser.api.UsersApi;
import com.example.sbuser.dto.UserReq;
import com.example.sbuser.usecase.UserUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserV1Controller implements UsersApi {
  private final UserUseCase userUseCase;

  @Override
  public ResponseEntity<List<UserReq>> getUsers() {
    List<UserReq> userEntityList = userUseCase.getUsers();
    return ResponseEntity.status(HttpStatus.CREATED).body(userEntityList);
  }

  @Override
  public ResponseEntity<Object> searchUsers(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    Page<UserReq> userEntityList = userUseCase.searchUser(size, page, sortDir, query, sortBy);
    return ResponseEntity.status(HttpStatus.CREATED).body(userEntityList);
  }
}
