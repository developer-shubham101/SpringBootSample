package com.example.sbbloginfo.service;

import com.example.sbbloginfo.dto.BlogResponse;
import com.example.sbbloginfo.entity.BlogEntity;
import com.example.sbbloginfo.exceptions.ResourceNotFoundException;
import com.example.sbbloginfo.mapper.BlogMapper;
import com.example.sbbloginfo.repository.BlogRepository;
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
public class BlogService {
  private final MongoTemplate template;
  private final BlogMapper blogMapper; // @RequiredArgsConstructor will create constructor
  private static final QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();

  private final BlogRepository blogRepository;

  public Page<BlogResponse> searchBlog(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    Criteria criteria;
    Query dynamicQuery;
    if (!StringUtils.isBlank(query)) {
      Condition<GeneralQueryBuilder> condition = pipeline.apply(query, BlogEntity.class);
      criteria = condition.query(new MongoVisitor());

      dynamicQuery = new Query();
      dynamicQuery.addCriteria(criteria);

    } else {
      dynamicQuery = new Query();
    }

    long count = template.count(dynamicQuery, BlogEntity.class);
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

    List<BlogEntity> blogEntities = template.find(dynamicQuery, BlogEntity.class);

    List<BlogResponse> blogs = blogMapper.mapToResponseBeans(blogEntities);

    return PageableExecutionUtils.getPage(blogs, pageable, () -> count);
  }

  public List<BlogEntity> getBlogs() {
    return blogRepository.findAll();
  }

  public List<BlogEntity> getBlogsByUser(String userId) {
    return blogRepository.findAllByUserId(userId);
  }

  public BlogEntity getBlogsById(String blogId) {
    return blogRepository
        .findById(blogId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "User with given id is not found on server !! : " + blogId));
  }

  public BlogEntity getBlogById(String blogId) {
    return blogRepository
        .findById(blogId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "User with given id is not found on server !! : " + blogId));
  }
}
