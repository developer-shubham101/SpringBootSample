package com.example.sbuser.entity;

import java.util.Date;
import lombok.Data;

@Data
public class CommentEntity {
  private String id;

  private String userId;
  private String blogId;
  private String content;
  private Date createdAt;
  private Date updatedAt;
}
