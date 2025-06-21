package com.example.hibernatebootcamp.service;

import com.example.hibernatebootcamp.dto.AuthorDto;
import com.example.hibernatebootcamp.dto.BlogRequestDto;
import com.example.hibernatebootcamp.dto.BlogResponseDto;
import com.example.hibernatebootcamp.dto.CategoryDto;
import com.example.hibernatebootcamp.entity.BlogEntity;
import com.example.hibernatebootcamp.entity.CategoryEntity;
import com.example.hibernatebootcamp.entity.UserEntity;
import com.example.hibernatebootcamp.exception.ResourceNotFoundException;
import com.example.hibernatebootcamp.repository.BlogRepository;
import com.example.hibernatebootcamp.repository.CategoryRepository;
import com.example.hibernatebootcamp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    @Transactional
    public BlogResponseDto createBlog(BlogRequestDto blogRequestDto) {
        UserEntity author = userRepository.findById(blogRequestDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + blogRequestDto.getAuthorId()));

        Set<CategoryEntity> categories = new HashSet<>(categoryRepository.findAllById(blogRequestDto.getCategoryIds()));
        if (categories.size() != blogRequestDto.getCategoryIds().size()) {
            throw new ResourceNotFoundException("One or more categories not found.");
        }

        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle(blogRequestDto.getTitle());
        blogEntity.setContent(blogRequestDto.getContent());
        blogEntity.setAuthor(author);
        blogEntity.setCategories(categories);
        blogEntity.setPublishedDate(LocalDateTime.now());

        BlogEntity savedBlogEntity = blogRepository.save(blogEntity);
        return convertToDto(savedBlogEntity);
    }

    @Transactional(readOnly = true)
    public BlogResponseDto getBlogById(Long id) {
        BlogEntity blogEntity = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        return convertToDto(blogEntity);
    }

    @Transactional(readOnly = true)
    public List<BlogResponseDto> getBlogsByTitle(String title) {
        List<BlogEntity> blogEntities = blogRepository.findByTitleContainingIgnoreCase(title);
        return blogEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BlogResponseDto> getBlogsByAuthorId(Long authorId) {
        List<BlogEntity> blogEntities = blogRepository.findByAuthorId(authorId);
        return blogEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BlogResponseDto> getBlogsByCategoryId(Long categoryId) {
        List<BlogEntity> blogEntities = blogRepository.findByCategoriesId(categoryId);
        return blogEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BlogResponseDto> getAllBlogs() {
        return blogRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public BlogResponseDto updateBlog(Long id, BlogRequestDto blogRequestDto) {
        BlogEntity blogEntity = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));

        UserEntity author = userRepository.findById(blogRequestDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + blogRequestDto.getAuthorId()));

        Set<CategoryEntity> categories = new HashSet<>(categoryRepository.findAllById(blogRequestDto.getCategoryIds()));
        if (categories.size() != blogRequestDto.getCategoryIds().size()) {
            throw new ResourceNotFoundException("One or more categories not found.");
        }

        blogEntity.setTitle(blogRequestDto.getTitle());
        blogEntity.setContent(blogRequestDto.getContent());
        blogEntity.setAuthor(author);
        blogEntity.setCategories(categories);

        BlogEntity updatedBlogEntity = blogRepository.save(blogEntity);
        return convertToDto(updatedBlogEntity);
    }

    @Transactional
    public void deleteBlog(Long id) {
        if (!blogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Blog not found with id: " + id);
        }
        blogRepository.deleteById(id);
    }

    private BlogResponseDto convertToDto(BlogEntity blogEntity) {
        BlogResponseDto blogResponseDto = new BlogResponseDto();
        blogResponseDto.setId(blogEntity.getId());
        blogResponseDto.setTitle(blogEntity.getTitle());
        blogResponseDto.setContent(blogEntity.getContent());
        blogResponseDto.setPublishedDate(blogEntity.getPublishedDate());

        AuthorDto authorDto = AuthorDto.builder()
                .id(blogEntity.getAuthor().getId())
                .username(blogEntity.getAuthor().getUsername())
                .email(blogEntity.getAuthor().getEmail())
                .build();
        blogResponseDto.setAuthor(authorDto);

        Set<CategoryDto> categoryDtos = blogEntity.getCategories().stream()
                .map(categoryEntity -> new CategoryDto(categoryEntity.getId(), categoryEntity.getName()))
                .collect(Collectors.toSet());
        blogResponseDto.setCategories(categoryDtos);

        return blogResponseDto;
    }
} 