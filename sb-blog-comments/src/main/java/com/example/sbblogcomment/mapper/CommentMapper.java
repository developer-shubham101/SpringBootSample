package com.example.sbblogcomment.mapper;

import com.example.sbblogcomment.entity.CommentEntity;
import com.example.sbblogcomments.dto.CommentRequest;
import com.example.sbblogcomments.dto.CommentResponse;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
  //    @Mapping(source = "id", target = "id")
  //    @Mapping(source = "username", target = "username")
  CommentResponse mapToResponseEntity(CommentEntity sourceBean);

  default List<CommentResponse> mapToResponseEntity(List<CommentEntity> sourceBeans) {
    if (sourceBeans == null) {
      return null;
    }

    ArrayList<CommentResponse> targetBeans = new ArrayList<>();
    for (CommentEntity sourceBean : sourceBeans) {
      targetBeans.add(mapToResponseEntity(sourceBean));
    }
    return targetBeans;
  }

  CommentEntity mapToEntity(CommentRequest commentRequest);
}
