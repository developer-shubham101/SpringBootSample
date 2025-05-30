package com.example.reactive.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomEpisode {
    private String title;
    private String writers;
    private String publishDate;
    private String description;
    private int id;
}