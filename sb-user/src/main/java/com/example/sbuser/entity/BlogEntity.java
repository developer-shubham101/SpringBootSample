package com.example.sbuser.entity;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BlogEntity {
  /** Unique identifier for the blog. */
  private String id;

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

  private List<CommentEntity> comments;
}
