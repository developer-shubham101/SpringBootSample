package com.example.reactive;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private final BlogService blogService;

    /****
     * Constructs a BlogController with the specified BlogService.
     *
     * @param blogService the service used to handle blog operations
     */
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    /**
     * Creates a new blog entry.
     *
     * @param blog the blog data to create
     * @return a Mono emitting the created blog
     */
    @PostMapping
    public Mono<Blog> createBlog(@RequestBody Blog blog) {
        return blogService.createBlog(blog);
    }

    /**
     * Retrieves all blog entries as a reactive stream.
     *
     * @return a Flux emitting all Blog entities
     */
    @GetMapping
    public Flux<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    /**
     * Retrieves a blog by its unique identifier.
     *
     * @param id the unique identifier of the blog to retrieve
     * @return a Mono emitting the blog with the specified ID, or empty if not found
     */
    @GetMapping("/{id}")
    public Mono<Blog> getBlogById(@PathVariable String id) {
        return blogService.getBlogById(id);
    }

    /**
     * Retrieves all blogs authored by the specified author as a reactive stream.
     *
     * @param author the name of the author whose blogs are to be retrieved
     * @return a Flux emitting blogs written by the given author
     */
    @GetMapping("/author/{author}")
    public Flux<Blog> getBlogsByAuthor(@PathVariable String author) {
        return blogService.getBlogsByAuthor(author);
    }

    /**
     * Updates an existing blog with the specified ID using the provided blog data.
     *
     * @param id the unique identifier of the blog to update
     * @param blog the new blog data to apply
     * @return a Mono emitting the updated blog
     */
    @PutMapping("/{id}")
    public Mono<Blog> updateBlog(@PathVariable String id, @RequestBody Blog blog) {
        return blogService.updateBlog(id, blog);
    }

    /**
     * Deletes a blog with the specified ID.
     *
     * @param id the unique identifier of the blog to delete
     * @return a Mono signaling completion when the blog is deleted
     */
    @DeleteMapping("/{id}")
    public Mono<Void> deleteBlog(@PathVariable String id) {
        return blogService.deleteBlog(id);
    }
}
