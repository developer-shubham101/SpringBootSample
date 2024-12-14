package com.example.sbblogcomment.entity;

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data // this annotation will create Getters and setters
@Document(collection = "comments")
public class CommentEntity {
  /** Unique identifier for the blog. */
  @Id private String id;

  private String userId;
  private String blogId;
  private String content;
  private Date createdAt;
  private Date updatedAt;
}
