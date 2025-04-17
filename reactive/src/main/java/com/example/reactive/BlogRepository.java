package com.example.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BlogRepository extends ReactiveMongoRepository<Blog, String> {
    /****
 * Returns a reactive stream of blog entries authored by the specified user.
 *
 * @param author the author's name to filter blog entries by
 * @return a Flux emitting all Blog entities with the given author
 */
Flux<Blog> findByAuthor(String author);
}
