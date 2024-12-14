package com.example.sbblogbusiness.service;

import com.example.sbblogbusiness.dto.CommentRequest;
import com.example.sbblogbusiness.dto.CommentResponse;
import com.example.sbblogbusiness.entity.BlogEntity;
import com.example.sbblogbusiness.entity.CommentEntity;
import com.example.sbblogbusiness.exceptions.ResourceNotFoundException;
import com.example.sbblogbusiness.mapper.BlogMapper;
import com.example.sbblogbusiness.repository.BlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
@CrossOrigin(origins = "*")
public class BlogService {

  private final BlogRepository blogRepository;
  private final RestTemplate restTemplate;
  private final BlogMapper blogMapper;

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

  public BlogEntity getBlogsById(String blogId) {
    return blogRepository
        .findById(blogId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Blog with given id is not found on server !! : " + blogId));
  }

  public CommentResponse createNewComment(String blogId, CommentRequest commentRequest) {

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HashMap<String, Object> map = new HashMap<>();

      map.put("blogId", blogId);
      map.put("userId", commentRequest.getUserId());
      map.put("content", commentRequest.getContent());

      ObjectMapper objectMapper = new ObjectMapper();
      // Convert the HashMap to JSON
      String json = objectMapper.writeValueAsString(map);

      HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

      CommentEntity commentResponse =
          restTemplate.postForObject(
              "http://SB-BLOG-COMMENTS/comments/v1/", requestEntity, CommentEntity.class);

      return blogMapper.mapToCommentModel(commentResponse);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
