package com.example.reactive.model;

import lombok.Data;

@Data
public class EpisodeResponse {
    private String title;
    private String writers;
    private String originalAirDate;
    private String desc;
    private int id;
}
