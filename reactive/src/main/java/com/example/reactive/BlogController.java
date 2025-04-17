package com.example.reactive;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public Mono<ResponseEntity<Blog>> createBlog(@RequestBody Blog blog) {
        return blogService.createBlog(blog)
                .map(saved -> ResponseEntity
                        .created(URI.create("/blogs/" + saved.getId()))
                        .body(saved));
    }

    @GetMapping
    public Flux<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Blog>> getBlogById(@PathVariable String id) {
        return blogService.getBlogById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/author/{author}")
    public Flux<Blog> getBlogsByAuthor(@PathVariable String author) {
        return blogService.getBlogsByAuthor(author);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Blog>> updateBlog(@PathVariable String id, @RequestBody Blog blog) {
        return blogService.updateBlog(id, blog)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteBlog(@PathVariable String id) {
        return blogService.deleteBlog(id).flatMap(response -> {
            if (response) {
                return Mono.just(ResponseEntity.noContent().build());
            } else {
                return Mono.just(ResponseEntity.notFound().build());
            }
        });
    }
}
