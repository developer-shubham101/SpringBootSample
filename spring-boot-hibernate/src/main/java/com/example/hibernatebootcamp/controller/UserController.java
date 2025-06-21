package com.example.hibernatebootcamp.controller;

import com.example.hibernatebootcamp.api.UsersApi;
import com.example.hibernatebootcamp.dto.UserReq;
import com.example.hibernatebootcamp.dto.UserRes;
import com.example.hibernatebootcamp.entity.UserEntity;
import com.example.hibernatebootcamp.mapper.UserMapper;
import com.example.hibernatebootcamp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController implements UsersApi {
    private final UserService userService;

    private final UserMapper userMapper;


    @Override
    public ResponseEntity<UserRes> createUser(UserReq userReq) {
        UserEntity userEntity = userMapper.toEntity(userReq);
        return ResponseEntity.ok(userMapper.toUserRes(userService.saveUser(userEntity)));
    }

    @Override
    public ResponseEntity<List<UserRes>> getAllUsers() {
        return ResponseEntity.ok(userMapper.toUserResList(userService.getAllUsers()));
    }
} 