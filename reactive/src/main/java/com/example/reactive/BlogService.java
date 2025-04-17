package com.example.reactive;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public Mono<Blog> createBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Flux<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Mono<Blog> getBlogById(String id) {
        return blogRepository.findById(id);
    }

    public Flux<Blog> getBlogsByAuthor(String author) {
        return blogRepository.findByAuthor(author);
    }

    public Mono<Blog> updateBlog(String id, Blog blog) {
        return blogRepository.findById(id)
                .flatMap(existingBlog -> {
                    existingBlog.setTitle(blog.getTitle());
                    existingBlog.setContent(blog.getContent());
                    existingBlog.setAuthor(blog.getAuthor());
                    return blogRepository.save(existingBlog);
                });
    }

    public Mono<Void> deleteBlog(String id) {
        return blogRepository.deleteById(id);
    }
}
