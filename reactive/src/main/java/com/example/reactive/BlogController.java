package com.example.reactive;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public Mono<Blog> createBlog(@RequestBody Blog blog) {
        return blogService.createBlog(blog);
    }

    @GetMapping
    public Flux<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/{id}")
    public Mono<Blog> getBlogById(@PathVariable String id) {
        return blogService.getBlogById(id);
    }

    @GetMapping("/author/{author}")
    public Flux<Blog> getBlogsByAuthor(@PathVariable String author) {
        return blogService.getBlogsByAuthor(author);
    }

    @PutMapping("/{id}")
    public Mono<Blog> updateBlog(@PathVariable String id, @RequestBody Blog blog) {
        return blogService.updateBlog(id, blog);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteBlog(@PathVariable String id) {
        return blogService.deleteBlog(id);
    }
}
