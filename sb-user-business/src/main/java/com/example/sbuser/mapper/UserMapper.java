package com.example.sbuser.mapper;

import com.example.sbuser.dto.UserReq;
import com.example.sbuser.entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
  //    @Mapping(source = "id", target = "id")
  //    @Mapping(source = "username", target = "username")
  UserReq mapToResponseBean(UserEntity sourceBean);

  default List<UserReq> mapToResponseBeans(List<UserEntity> sourceBeans) {
    if (sourceBeans == null) {
      return null;
    }

    ArrayList<UserReq> targetBeans = new ArrayList<>();
    for (UserEntity sourceBean : sourceBeans) {
      targetBeans.add(mapToResponseBean(sourceBean));
    }
    return targetBeans;
  }
}
