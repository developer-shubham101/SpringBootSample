package com.example.hibernatebootcamp.mapper;

import com.example.hibernatebootcamp.dto.CategoryDto;
import com.example.hibernatebootcamp.dto.CategoryReq;
import com.example.hibernatebootcamp.dto.CategoryRes;
import com.example.hibernatebootcamp.entity.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toCategoryDto(CategoryReq categoryReq);

    CategoryEntity toEntity(CategoryDto categoryDto);

    CategoryDto toDto(CategoryEntity categoryEntity);

    List<CategoryDto> toDto(List<CategoryEntity> categoryEntity);

    CategoryRes toCategoryRes(CategoryDto categoryDto);

    List<CategoryRes> toCategoryResList(List<CategoryDto> categoryDtos);
} 