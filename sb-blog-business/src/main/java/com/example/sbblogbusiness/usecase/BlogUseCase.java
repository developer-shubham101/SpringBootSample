package com.example.sbblogbusiness.usecase;

import com.example.sbblogbusiness.dto.BlogReq;
import com.example.sbblogbusiness.entity.BlogEntity;
import com.example.sbblogbusiness.mapper.BlogMapper;
import com.example.sbblogbusiness.service.BlogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogUseCase {
  private final BlogMapper blogMapper; // @RequiredArgsConstructor will create constructor
  @Autowired private BlogService blogService;

  public List<BlogReq> getBlogs() {
    List<BlogEntity> blogEntityList = blogService.getBlogs();
    return blogMapper.mapToResponseBeans(blogEntityList);
  }

  public Page<BlogReq> searchBlogs(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    return blogService.searchBlog(size, page, sortDir, query, sortBy);
  }
}
