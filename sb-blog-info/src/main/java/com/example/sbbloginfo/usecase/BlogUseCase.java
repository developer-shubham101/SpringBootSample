package com.example.sbbloginfo.usecase;

import com.example.sbbloginfo.dto.BlogResponse;
import com.example.sbbloginfo.dto.CommentRes;
import com.example.sbbloginfo.entity.BlogEntity;
import com.example.sbbloginfo.entity.CommentEntity;
import com.example.sbbloginfo.mapper.BlogMapper;
import com.example.sbbloginfo.service.BlogService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BlogUseCase {
  private final BlogMapper blogMapper; // @RequiredArgsConstructor will create constructor
  private final BlogService blogService;
  private final RestTemplate restTemplate;

  public List<BlogResponse> getBlogs() {
    List<BlogEntity> blogEntityList = blogService.getBlogs();

    return blogEntityList.stream().map(this::getBlogComments).toList();
  }

  public Page<BlogResponse> searchBlogs(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    return blogService.searchBlog(size, page, sortDir, query, sortBy);
  }

  public List<BlogResponse> getBlogsByUser(String userId) {
    List<BlogEntity> blogEntity = blogService.getBlogsByUser(userId);

    return blogEntity.stream().map(this::getBlogComments).toList();
  }

  private BlogResponse getBlogComments(BlogEntity blogEntity) {
    CommentEntity[] commentEntities =
        restTemplate.getForObject(
            "http://SB-BLOG-COMMENTS/comments/v1/blog/" + blogEntity.getId(),
            CommentEntity[].class);

    List<CommentRes> commentRes =
        blogMapper.mapToCommentResponseBeans(Arrays.stream(commentEntities).toList());

    BlogResponse blogRes = blogMapper.mapToResponseEntity(blogEntity);
    blogRes.setComments(commentRes);
    return blogRes;
  }

  public BlogResponse getBlogsById(String blogId) {
    BlogEntity blogEntityList = blogService.getBlogById(blogId);

    return getBlogComments(blogEntityList);
  }
}
