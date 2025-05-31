package com.example.reactive.repository;

import com.example.reactive.model.Blog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BlogRepository extends ReactiveMongoRepository<Blog, String> {
    Flux<Blog> findByAuthor(String author);
}
