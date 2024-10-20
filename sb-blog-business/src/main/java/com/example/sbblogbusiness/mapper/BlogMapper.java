package com.example.sbblogbusiness.mapper;

import com.example.sbblogbusiness.dto.BlogReq;
import com.example.sbblogbusiness.entity.BlogEntity;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BlogMapper {
  //    @Mapping(source = "id", target = "id")
  //    @Mapping(source = "username", target = "username")
  BlogReq mapToResponseBean(BlogEntity sourceBean);

  default List<BlogReq> mapToResponseBeans(List<BlogEntity> sourceBeans) {
    if (sourceBeans == null) {
      return null;
    }

    ArrayList<BlogReq> targetBeans = new ArrayList<>();
    for (BlogEntity sourceBean : sourceBeans) {
      targetBeans.add(mapToResponseBean(sourceBean));
    }
    return targetBeans;
  }
}
