package com.example.sbblogbusiness.entity;

import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data // this annotation will create Getters and setters
@Document(collection = "blogs")
public class BlogEntity {
  /** Unique identifier for the blog. */
  @Id private String id;

  /** Unique identifier for the blog. */
  private String userId;

  /** Title of the blog. */
  private String title;

  /** Content of the blog. */
  private String content;

  /** Author of the blog. */
  private String author;

  /** Date and time when the blog was created. */
  private Date createdAt;

  /** Date and time when the blog was last updated. */
  private Date updatedAt;

  /** Categories associated with the blog. */
  private List<String> categories;

  /** Tags associated with the blog. */
  private List<String> tags;
}
