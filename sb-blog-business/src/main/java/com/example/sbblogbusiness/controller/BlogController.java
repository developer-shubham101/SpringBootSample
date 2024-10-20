package com.example.sbblogbusiness.controller;

import com.example.sbblogbusiness.entity.BlogEntity;
import com.example.sbblogbusiness.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

  @Autowired private BlogService blogService;

  @PostMapping
  public ResponseEntity<BlogEntity> createNewBlog(@RequestBody BlogEntity requestBody) {
    System.out.println("createNewBlog");
    System.out.println(requestBody);
    BlogEntity createdBlogEntity = blogService.createBlog(requestBody);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdBlogEntity);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BlogEntity> updateBlog(
      @PathVariable String id, @RequestBody BlogEntity requestBody) {
    BlogEntity updatedUserEntity = blogService.updateBlog(id, requestBody);
    if (updatedUserEntity != null) {
      return ResponseEntity.ok(updatedUserEntity);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBlog(@PathVariable String id) {
    blogService.deleteBlog(id);
    return ResponseEntity.noContent().build();
  }
}
