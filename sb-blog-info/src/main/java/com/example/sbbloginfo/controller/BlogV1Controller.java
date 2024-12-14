package com.example.sbbloginfo.controller;

import com.example.sbbloginfo.api.BlogsApi;
import com.example.sbbloginfo.dto.BlogResponse;
import com.example.sbbloginfo.usecase.BlogUseCase;
import com.example.sbbloginfo.utility.RoleConstants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BlogV1Controller implements BlogsApi {
  private final BlogUseCase blogUseCase;

  @Override
  public ResponseEntity<BlogResponse> getBlogById(String blogId) {
    BlogResponse blogEntityList = blogUseCase.getBlogsById(blogId);
    return ResponseEntity.status(HttpStatus.OK).body(blogEntityList);
  }

  @Override
  @PreAuthorize(RoleConstants.INTERNAL)
  public ResponseEntity<List<BlogResponse>> getBlogsByUser(String userId) {
    List<BlogResponse> blogEntityList = blogUseCase.getBlogsByUser(userId);
    return ResponseEntity.status(HttpStatus.OK).body(blogEntityList);
  }

  @Override
  public ResponseEntity<List<BlogResponse>> getBlogs() {
    List<BlogResponse> blogEntityList = blogUseCase.getBlogs();
    return ResponseEntity.status(HttpStatus.OK).body(blogEntityList);
  }

  @Override
  public ResponseEntity<Object> searchBlogs(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    Page<BlogResponse> blogEntityList = blogUseCase.searchBlogs(size, page, sortDir, query, sortBy);
    return ResponseEntity.status(HttpStatus.OK).body(blogEntityList);
  }
}
