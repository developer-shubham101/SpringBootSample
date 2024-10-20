package com.example.sbbloginfo.controller;

import com.example.sbbloginfo.api.BlogsApi;
import com.example.sbbloginfo.dto.BlogReq;
import com.example.sbbloginfo.usecase.BlogUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BlogV1Controller implements BlogsApi {
  private final BlogUseCase blogUseCase;

  @Override
  public ResponseEntity<List<BlogReq>> getBlogs() {
    List<BlogReq> blogEntityList = blogUseCase.getBlogs();
    return ResponseEntity.status(HttpStatus.CREATED).body(blogEntityList);
  }

  @Override
  public ResponseEntity<Object> searchBlogs(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    Page<BlogReq> blogEntityList = blogUseCase.searchBlogs(size, page, sortDir, query, sortBy);
    return ResponseEntity.status(HttpStatus.CREATED).body(blogEntityList);
  }
}
