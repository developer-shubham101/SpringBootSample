package com.example.hibernatebootcamp.controller;

import com.example.hibernatebootcamp.api.CategoriesApi;
import com.example.hibernatebootcamp.dto.CategoryDto;
import com.example.hibernatebootcamp.dto.CategoryReq;
import com.example.hibernatebootcamp.dto.CategoryRes;
import com.example.hibernatebootcamp.mapper.CategoryMapper;
import com.example.hibernatebootcamp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing categories.
 */
@RestController
public class CategoryController implements CategoriesApi {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseEntity<CategoryRes> createCategory(@RequestBody CategoryReq categoryReq) {
        CategoryDto categoryDto = categoryMapper.toCategoryDto(categoryReq);
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(categoryMapper.toCategoryRes(createdCategory));
    }

    @Override
    public ResponseEntity<List<CategoryRes>> getAllCategories() {
        List<CategoryDto> categoryDtos = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryMapper.toCategoryResList(categoryDtos));
    }
} 