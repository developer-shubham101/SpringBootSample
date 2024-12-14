package com.example.sbblogbusiness.controller;

import com.example.sbblogbusiness.api.BlogsApi;
import com.example.sbblogbusiness.dto.BlogReq;
import com.example.sbblogbusiness.dto.BlogRes;
import com.example.sbblogbusiness.dto.CommentRequest;
import com.example.sbblogbusiness.dto.CommentResponse;
import com.example.sbblogbusiness.usecase.BlogUseCase;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<BlogRes> createNewBlog(BlogReq blogReq) {
    System.out.println("createNewBlog");
    System.out.println(blogReq);
    BlogRes createdBlogEntity = blogUseCase.createBlog(blogReq);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdBlogEntity);
  }

  @Override
  public ResponseEntity<BlogRes> updateBlog(String blogId, BlogReq blogReq) {
    BlogRes updatedUserEntity = blogUseCase.updateBlog(blogId, blogReq);
    if (updatedUserEntity != null) {
      return ResponseEntity.ok(updatedUserEntity);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Override
  public ResponseEntity<Void> deleteBlog(String blogId) {
    blogUseCase.deleteBlog(blogId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<CommentResponse> createBlogComment(
      String blogId, CommentRequest commentRequest) {
    CommentResponse createdBlogEntity = blogUseCase.createNewComment(blogId, commentRequest);

    return ResponseEntity.ok(createdBlogEntity);
  }
}
