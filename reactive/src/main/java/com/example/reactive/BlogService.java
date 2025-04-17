package com.example.reactive;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BlogService {
    private final BlogRepository blogRepository;

    /****
     * Constructs a BlogService with the specified BlogRepository for data access.
     *
     * @param blogRepository the repository used for blog persistence operations
     */
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    /**
     * Creates a new blog entry and saves it to the repository.
     *
     * @param blog the blog entity to be created
     * @return a Mono emitting the saved blog entity
     */
    public Mono<Blog> createBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    /**
     * Retrieves all blog entries as a reactive stream.
     *
     * @return a Flux emitting all Blog entities
     */
    public Flux<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    /**
     * Retrieves a blog entry by its unique identifier.
     *
     * @param id the unique identifier of the blog
     * @return a Mono emitting the blog with the specified ID, or empty if not found
     */
    public Mono<Blog> getBlogById(String id) {
        return blogRepository.findById(id);
    }

    /**
     * Retrieves all blogs authored by the specified author.
     *
     * @param author the name of the author whose blogs are to be retrieved
     * @return a Flux emitting all Blog entries written by the given author
     */
    public Flux<Blog> getBlogsByAuthor(String author) {
        return blogRepository.findByAuthor(author);
    }

    /**
     * Updates an existing blog with new title, content, and author information.
     *
     * @param id the unique identifier of the blog to update
     * @param blog the blog object containing updated fields
     * @return a Mono emitting the updated Blog, or empty if the blog does not exist
     */
    public Mono<Blog> updateBlog(String id, Blog blog) {
        return blogRepository.findById(id)
                .flatMap(existingBlog -> {
                    existingBlog.setTitle(blog.getTitle());
                    existingBlog.setContent(blog.getContent());
                    existingBlog.setAuthor(blog.getAuthor());
                    return blogRepository.save(existingBlog);
                });
    }

    /**
     * Deletes the blog entry with the specified ID.
     *
     * @param id the unique identifier of the blog to delete
     * @return a Mono signaling completion when the blog is deleted
     */
    public Mono<Void> deleteBlog(String id) {
        return blogRepository.deleteById(id);
    }
}
