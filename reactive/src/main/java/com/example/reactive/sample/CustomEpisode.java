package com.example.reactive.sample;

public class CustomEpisode {
    public String title;
    public String writers;
    public String publishDate;
    public String description;
    public int id;

    /**
     * Constructs a CustomEpisode with the specified title, writers, publish date, description, and ID.
     *
     * @param title the episode's title
     * @param writers the episode's writers
     * @param publishDate the episode's publish date
     * @param description a brief description of the episode
     * @param id the unique identifier for the episode
     */
    public CustomEpisode(String title, String writers, String publishDate, String description, int id) {
        this.title = title;
        this.writers = writers;
        this.publishDate = publishDate;
        this.description = description;
        this.id = id;
    }
}