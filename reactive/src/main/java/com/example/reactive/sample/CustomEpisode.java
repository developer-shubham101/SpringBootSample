package com.example.reactive.sample;

public class CustomEpisode {
    public String title;
    public String writers;
    public String publishDate;
    public String description;
    public int id;

    public CustomEpisode(String title, String writers, String publishDate, String description, int id) {
        this.title = title;
        this.writers = writers;
        this.publishDate = publishDate;
        this.description = description;
        this.id = id;
    }
}