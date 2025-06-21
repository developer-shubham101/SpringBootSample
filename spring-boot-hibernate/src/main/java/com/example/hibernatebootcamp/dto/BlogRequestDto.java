package com.example.hibernatebootcamp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class BlogRequestDto {
    // Getters and Setters
    private String title;
    private String content;
    private Long authorId;
    private Set<Long> categoryIds;

}