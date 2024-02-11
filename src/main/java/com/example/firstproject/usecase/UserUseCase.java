package com.example.firstproject.usecase;

import com.example.firstproject.dto.UserReq;
import com.example.firstproject.entity.UserEntity;
import com.example.firstproject.mapper.UserMapper;
import com.example.firstproject.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUseCase {
  private final UserMapper userMapper; // @RequiredArgsConstructor will create constructor
  @Autowired private UserService userService;

  public List<UserReq> getUsers() {
    List<UserEntity> userEntityList = userService.getUsers();
    return userMapper.mapToResponseBeans(userEntityList);
  }

  public Page<UserReq> searchUser(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    return userService.searchUser(size, page, sortDir, query, sortBy);
  }
}
