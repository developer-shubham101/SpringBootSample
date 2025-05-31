package com.example.reactive.dto;

import com.example.reactive.model.Product;
import com.example.reactive.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {
    private Product product;
    private List<Review> reviews;
} 