package com.example.hibernatebootcamp.mapper;

import com.example.hibernatebootcamp.dto.BlogReq;
import com.example.hibernatebootcamp.dto.BlogRequestDto;
import com.example.hibernatebootcamp.dto.BlogRes;
import com.example.hibernatebootcamp.dto.BlogResponseDto;
import com.example.hibernatebootcamp.entity.BlogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class})
public interface BlogMapper {

    @Mapping(source = "author", target = "author")
    @Mapping(source = "categories", target = "categories")
    BlogRes toBlogRes(BlogResponseDto blog);

    List<BlogRes> toBlogResList(List<BlogResponseDto> blogs);

    BlogEntity toEntity(BlogReq blogReq);

    BlogRequestDto toBlogRequestDto(BlogReq blogReq);

    default OffsetDateTime map(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atOffset(ZoneOffset.UTC);
    }
}