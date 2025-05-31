package com.example.reactive.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "blogs")
public class Blog {
    @Id
    private String id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt = LocalDateTime.now();
}
