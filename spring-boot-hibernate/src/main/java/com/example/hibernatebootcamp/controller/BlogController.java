package com.example.hibernatebootcamp.controller;

import com.example.hibernatebootcamp.api.BlogsApi;
import com.example.hibernatebootcamp.dto.BlogReq;
import com.example.hibernatebootcamp.dto.BlogRes;
import com.example.hibernatebootcamp.dto.BlogResponseDto;
import com.example.hibernatebootcamp.mapper.BlogMapper;
import com.example.hibernatebootcamp.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BlogController implements BlogsApi {


    private final BlogService blogService;


    private final BlogMapper blogMapper;

    @Override
    public ResponseEntity<BlogRes> createBlog(BlogReq blogReq) {
        BlogResponseDto createdBlog = blogService.createBlog(blogMapper.toBlogRequestDto(blogReq));
        return ResponseEntity.ok(blogMapper.toBlogRes(createdBlog));
    }

    @Override
    public ResponseEntity<List<BlogRes>> getAllBlogs() {
        return ResponseEntity.ok(blogMapper.toBlogResList(blogService.getAllBlogs()));
    }

    @Override
    public ResponseEntity<Void> deleteBlog(Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<BlogRes> updateBlog(Long id, BlogReq blogReq) {
        BlogResponseDto updatedBlog = blogService.updateBlog(id, blogMapper.toBlogRequestDto(blogReq));
        return ResponseEntity.ok(blogMapper.toBlogRes(updatedBlog));
    }

    @Override
    public ResponseEntity<List<BlogRes>> getBlogsByCategoryId(Long categoryId) {
        List<BlogResponseDto> blogDtos = blogService.getBlogsByCategoryId(categoryId);
        return ResponseEntity.ok(blogMapper.toBlogResList(blogDtos));
    }

    @Override
    public ResponseEntity<BlogRes> getBlogById(Long id) {
        BlogResponseDto blogDto = blogService.getBlogById(id);
        return ResponseEntity.ok(blogMapper.toBlogRes(blogDto));
    }

    @Override
    public ResponseEntity<List<BlogRes>> getBlogsByTitle(String title) {
        List<BlogResponseDto> blogDtos = blogService.getBlogsByTitle(title);
        return ResponseEntity.ok(blogMapper.toBlogResList(blogDtos));
    }

    @Override
    public ResponseEntity<List<BlogRes>> getBlogsByUserId(Long userId) {
        List<BlogResponseDto> blogDtos = blogService.getBlogsByAuthorId(userId);
        return ResponseEntity.ok(blogMapper.toBlogResList(blogDtos));
    }
} 