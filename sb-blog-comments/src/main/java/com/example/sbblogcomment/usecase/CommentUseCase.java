package com.example.sbblogcomment.usecase;

import com.example.sbblogcomment.entity.CommentEntity;
import com.example.sbblogcomment.mapper.CommentMapper;
import com.example.sbblogcomment.service.CommentService;
import com.example.sbblogcomments.dto.CommentRequest;
import com.example.sbblogcomments.dto.CommentResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentUseCase {
  private final CommentMapper commentMapper; // @RequiredArgsConstructor will create constructor
  @Autowired private CommentService commentService;

  public List<CommentResponse> getComments() {
    List<CommentEntity> commentEntityList = commentService.getComments();
    return commentMapper.mapToResponseEntity(commentEntityList);
  }

  public Page<CommentResponse> searchComments(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    return commentService.searchComments(size, page, sortDir, query, sortBy);
  }

  public CommentResponse createComment(CommentRequest commentRequest) {

    CommentEntity requestEntity = commentMapper.mapToEntity(commentRequest);

    CommentEntity tmpCommentEntity = commentService.createComment(requestEntity);

    return commentMapper.mapToResponseEntity(tmpCommentEntity);
  }

  public CommentResponse updateComment(String commentId, CommentRequest commentRequest) {
    CommentEntity requestEntity = commentMapper.mapToEntity(commentRequest);

    CommentEntity tmpCommentEntity = commentService.updateComment(commentId, requestEntity);
    return commentMapper.mapToResponseEntity(tmpCommentEntity);
  }

  public void deleteComment(String commentId) {
    commentService.deleteComment(commentId);
  }

  public List<CommentResponse> getBlogsByBlog(String blogId) {
    List<CommentEntity> commentEntityList = commentService.getCommentsByBlog(blogId);
    return commentMapper.mapToResponseEntity(commentEntityList);
  }
}
