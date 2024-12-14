package com.example.sbblogbusiness.usecase;

import com.example.sbblogbusiness.dto.BlogReq;
import com.example.sbblogbusiness.dto.BlogRes;
import com.example.sbblogbusiness.dto.CommentRequest;
import com.example.sbblogbusiness.dto.CommentResponse;
import com.example.sbblogbusiness.entity.BlogEntity;
import com.example.sbblogbusiness.mapper.BlogMapper;
import com.example.sbblogbusiness.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogUseCase {
  private final BlogMapper blogMapper; // @RequiredArgsConstructor will create constructor
  private final BlogService blogService;

  public BlogRes createBlog(BlogReq blogReq) {
    BlogEntity blogEntity = blogMapper.mapToEntity(blogReq);
    BlogEntity newBlogEntity = blogService.createBlog(blogEntity);

    return blogMapper.mapToResEntity(newBlogEntity);
  }

  public BlogRes updateBlog(String blogId, BlogReq blogReq) {
    BlogEntity blogEntity = blogMapper.mapToEntity(blogReq);

    BlogEntity newBlogEntity = blogService.updateBlog(blogId, blogEntity);

    return blogMapper.mapToResEntity(newBlogEntity);
  }

  public void deleteBlog(String blogId) {

    blogService.deleteBlog(blogId);
  }

  public CommentResponse createNewComment(String blogId, CommentRequest commentRequest) {

    blogService.getBlogsById(blogId); // If blog exist

    return blogService.createNewComment(blogId, commentRequest);
  }
}
