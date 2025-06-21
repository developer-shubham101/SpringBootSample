package com.example.hibernatebootcamp.service;

import com.example.hibernatebootcamp.dto.CategoryDto;
import com.example.hibernatebootcamp.entity.CategoryEntity;
import com.example.hibernatebootcamp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryDto.getName());
        CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);
        return convertToDto(savedCategoryEntity);
    }

    private CategoryDto convertToDto(CategoryEntity categoryEntity) {
        return new CategoryDto(categoryEntity.getId(), categoryEntity.getName());
    }
} 