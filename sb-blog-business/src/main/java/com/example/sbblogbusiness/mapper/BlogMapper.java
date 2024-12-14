package com.example.sbblogbusiness.mapper;

import com.example.sbblogbusiness.dto.BlogReq;
import com.example.sbblogbusiness.dto.BlogRes;
import com.example.sbblogbusiness.dto.CommentResponse;
import com.example.sbblogbusiness.entity.BlogEntity;
import com.example.sbblogbusiness.entity.CommentEntity;
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

  BlogRes mapToResponseBean(BlogEntity sourceBean);

  default List<BlogRes> mapToResponseModelList(List<BlogEntity> sourceBeans) {
    if (sourceBeans == null) {
      return null;
    }

    ArrayList<BlogRes> targetBeans = new ArrayList<>();
    for (BlogEntity sourceBean : sourceBeans) {
      targetBeans.add(mapToResponseBean(sourceBean));
    }
    return targetBeans;
  }

  BlogRes mapToResEntity(BlogEntity blogEntity);

  BlogEntity mapToEntity(BlogReq blogReq);

  CommentResponse mapToCommentModel(CommentEntity commentResponse);
}
