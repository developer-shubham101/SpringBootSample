package com.example.sbbloginfo.mapper;

import com.example.sbbloginfo.dto.BlogResponse;
import com.example.sbbloginfo.dto.CommentRes;
import com.example.sbbloginfo.entity.BlogEntity;
import com.example.sbbloginfo.entity.CommentEntity;
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
  BlogResponse mapToResponseBean(BlogEntity sourceBean);

  default List<BlogResponse> mapToResponseBeans(List<BlogEntity> sourceBeans) {
    if (sourceBeans == null) {
      return null;
    }

    ArrayList<BlogResponse> targetBeans = new ArrayList<>();
    for (BlogEntity sourceBean : sourceBeans) {
      targetBeans.add(mapToResponseBean(sourceBean));
    }
    return targetBeans;
  }

  List<CommentRes> mapToCommentResponseBeans(List<CommentEntity> list);

  BlogResponse mapToResponseEntity(BlogEntity blogEntity);
}
