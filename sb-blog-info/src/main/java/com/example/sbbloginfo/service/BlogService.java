package com.example.sbbloginfo.service;

import com.example.sbbloginfo.dto.BlogReq;
import com.example.sbbloginfo.entity.BlogEntity;
import com.example.sbbloginfo.mapper.BlogMapper;
import com.example.sbbloginfo.repository.BlogRepository;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired private BlogRepository blogRepository;

  public Page<BlogReq> searchBlog(
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

    List<BlogReq> blogs = blogMapper.mapToResponseBeans(blogEntities);

    return PageableExecutionUtils.getPage(blogs, pageable, () -> count);
  }

  public List<BlogEntity> getBlogs() {
    return blogRepository.findAll();
  }

  public BlogEntity createBlog(BlogEntity blogEntity) {
    return blogRepository.save(blogEntity);
  }

  public BlogEntity updateBlog(String id, BlogEntity updatedBlogEntity) {
    BlogEntity existingBlogEntity = blogRepository.findById(id).orElse(null);
    if (existingBlogEntity != null) {
      existingBlogEntity.setTitle(updatedBlogEntity.getTitle());
      existingBlogEntity.setContent(updatedBlogEntity.getContent());
      return blogRepository.save(existingBlogEntity);
    }
    return null; // Blog not found
  }

  public void deleteBlog(String id) {
    blogRepository.deleteById(id);
  }
}
