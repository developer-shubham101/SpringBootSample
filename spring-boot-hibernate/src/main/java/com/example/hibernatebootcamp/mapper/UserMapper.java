package com.example.hibernatebootcamp.mapper;

import com.example.hibernatebootcamp.dto.UserReq;
import com.example.hibernatebootcamp.dto.UserRes;
import com.example.hibernatebootcamp.entity.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserRes toUserRes(UserEntity userEntity);

    UserEntity toEntity(UserReq userReq);

    List<UserRes> toUserResList(List<UserEntity> userEntities);
} 