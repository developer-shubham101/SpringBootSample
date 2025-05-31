package com.example.reactive.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private String productId;
    private String author;
    private int rating; // 1-5
    private String comment;
} 