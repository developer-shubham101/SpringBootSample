package com.example.bootcamp.usecase;

import com.example.bootcamp.dto.UserReq;
import com.example.bootcamp.entity.UserEntity;
import com.example.bootcamp.exceptions.UserNotFoundException;
import com.example.bootcamp.mapper.UserMapper;
import com.example.bootcamp.service.UserService;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class UserUseCase {

  private final UserMapper userMapper; // @RequiredArgsConstructor will create constructor
  @Autowired private UserService userService;

  @PostConstruct
  public void init() {
    // Initialization logic here, e.g., open a file or validate configurations
    System.out.println("Bean has been initialized!");
  }

  @PreDestroy
  public void cleanup() {
    // Cleanup logic here, e.g., close a connection or release resources
    System.out.println("Bean is about to be destroyed!");
  }

  public List<UserReq> getUsers() {
    List<UserEntity> userEntityList = userService.getUsers();
    return userMapper.mapToResponseEntityList(userEntityList);
  }

  public Page<UserReq> searchUser(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    return userService.searchUser(size, page, sortDir, query, sortBy);
  }

  public UserReq updateUser(UserReq userReq) {
    UserEntity userEntity = userMapper.mapFromReqToUserEntity(userReq);

    UserEntity updateUser = userService.updateUser(userEntity.getId(), userEntity);
    return userMapper.mapToResponseEntity(updateUser);
  }

  public void deleteUser(String id) {
    userService.deleteUser(id);
  }

  public UserReq getUser(String username) {
    Optional<UserEntity> user = userService.getUser(username);
    return user.map(userMapper::mapToResponseEntity)
        .orElseThrow(() -> new UserNotFoundException("User not found."));
  }

  public UserReq getUserById(String id) {
    Optional<UserEntity> user = userService.getUserById(id);
    return user.map(userMapper::mapToResponseEntity)
        .orElseThrow(() -> new UserNotFoundException("User not found with the given ID."));
  }

  public UserReq uploadUserProfile(MultipartFile file, String username) {
    UserEntity updateUser = userService.uploadUserProfile(file, username);

    return userMapper.mapToResponseEntity(updateUser);
  }
}