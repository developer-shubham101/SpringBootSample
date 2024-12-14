package com.example.sbblogcomment.service;

import com.example.sbblogcomment.entity.CommentEntity;
import com.example.sbblogcomment.mapper.CommentMapper;
import com.example.sbblogcomment.repository.CommentsRepository;
import com.example.sbblogcomments.dto.CommentResponse;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@RequiredArgsConstructor
@Service
@CrossOrigin(origins = "*")
public class CommentService {
  private final MongoTemplate template;
  public final CommentMapper commentMapper; // @RequiredArgsConstructor will create constructor
  private static final QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();
  private final CommentsRepository commentsRepository;

  public Page<CommentResponse> searchComments(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    Criteria criteria;
    Query dynamicQuery;
    if (!StringUtils.isBlank(query)) {
      Condition<GeneralQueryBuilder> condition = pipeline.apply(query, CommentEntity.class);
      criteria = condition.query(new MongoVisitor());

      dynamicQuery = new Query();
      dynamicQuery.addCriteria(criteria);

    } else {
      dynamicQuery = new Query();
    }

    long count = template.count(dynamicQuery, CommentEntity.class);
    PageRequest pageable;
    if (!StringUtils.isBlank(sortDir) && !StringUtils.isBlank(sortBy)) {
      pageable = PageRequest.of(page, size, Sort.Direction.valueOf(sortDir.toUpperCase()), sortBy);
    } else {
      pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
    }

    if (StringUtils.isBlank(sortBy)) {
      dynamicQuery.with(Sort.by(Sort.DEFAULT_DIRECTION, "id"));
    } else {
      dynamicQuery.with(Sort.by(sortBy));
    }
    dynamicQuery.with(pageable);

    List<CommentEntity> blogEntities = template.find(dynamicQuery, CommentEntity.class);

    List<CommentResponse> comments = commentMapper.mapToResponseEntity(blogEntities);

    return PageableExecutionUtils.getPage(comments, pageable, () -> count);
  }

  public CommentEntity createComment(CommentEntity updatedCommentEntity) {
    return commentsRepository.save(updatedCommentEntity);
  }

  public CommentEntity updateComment(String id, CommentEntity updatedCommentEntity) {
    CommentEntity existingCommentEntity = commentsRepository.findById(id).orElse(null);
    if (existingCommentEntity != null) {
      existingCommentEntity.setContent(updatedCommentEntity.getContent());
      return commentsRepository.save(existingCommentEntity);
    }
    return null; // Blog not found
  }

  public void deleteComment(String id) {
    commentsRepository.deleteById(id);
  }

  public List<CommentEntity> getCommentsByBlog(String userId) {

    return commentsRepository.findByBlogId(userId);
  }

  public List<CommentEntity> getComments() {
    return commentsRepository.findAll();
  }
}
