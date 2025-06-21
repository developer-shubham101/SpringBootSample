package com.example.hibernatebootcamp.service;

import com.example.hibernatebootcamp.dto.CategoryDto;
import com.example.hibernatebootcamp.entity.CategoryEntity;
import com.example.hibernatebootcamp.mapper.CategoryMapper;
import com.example.hibernatebootcamp.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categoryMapper.toDto(categories);
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new IllegalArgumentException("Category DTO cannot be null.");
        }
        if (categoryDto.getName() == null || categoryDto.getName().isBlank()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        CategoryEntity categoryEntity = categoryMapper.toEntity(categoryDto);
        CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);
        return categoryMapper.toDto(savedCategoryEntity);
    }
} 