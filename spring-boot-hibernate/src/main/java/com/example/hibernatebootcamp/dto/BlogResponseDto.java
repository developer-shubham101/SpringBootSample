package com.example.hibernatebootcamp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
public class BlogResponseDto {
    // Getters and Setters
    private Long id;
    private String title;
    private String content;
    private LocalDateTime publishedDate;
    private AuthorDto author;
    private Set<CategoryDto> categories;

}