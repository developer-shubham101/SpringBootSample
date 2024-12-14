package com.example.sbblogcomment.controller;

import com.example.sbblogcomment.usecase.CommentUseCase;
import com.example.sbblogcomments.api.CommentsApi;
import com.example.sbblogcomments.dto.CommentRequest;
import com.example.sbblogcomments.dto.CommentResponse;
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
public class CommentsV1Controller implements CommentsApi {
  private final CommentUseCase commentUseCase;

  @Override
  public ResponseEntity<List<CommentResponse>> getAllComments() {
    List<CommentResponse> blogEntityList = commentUseCase.getComments();
    return ResponseEntity.status(HttpStatus.CREATED).body(blogEntityList);
  }

  @Override
  public ResponseEntity<Object> searchComments(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    Page<CommentResponse> blogEntityList =
        commentUseCase.searchComments(size, page, sortDir, query, sortBy);
    return ResponseEntity.status(HttpStatus.CREATED).body(blogEntityList);
  }

  @Override
  public ResponseEntity<CommentResponse> createComment(CommentRequest commentRequest) {
    System.out.println("createNewBlog");
    System.out.println(commentRequest);
    CommentResponse createdCommentEntity = commentUseCase.createComment(commentRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentEntity);
  }

  @Override
  public ResponseEntity<CommentResponse> updateCommentById(
      String commentId, CommentRequest commentRequest) {
    CommentResponse updatedUserEntity = commentUseCase.updateComment(commentId, commentRequest);
    if (updatedUserEntity != null) {
      return ResponseEntity.ok(updatedUserEntity);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Override
  public ResponseEntity<Void> deleteCommentById(String commentId) {
    commentUseCase.deleteComment(commentId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<List<CommentResponse>> getCommentsByBlogId(String blogId) {
    List<CommentResponse> commentEntityList = commentUseCase.getBlogsByBlog(blogId);
    return ResponseEntity.status(HttpStatus.OK).body(commentEntityList);
  }
}
