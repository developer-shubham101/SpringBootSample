package com.example.sbblogcomment.repository;

import com.example.sbblogcomment.entity.CommentEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentsRepository extends MongoRepository<CommentEntity, String> {

  List<CommentEntity> findByBlogId(String blogId);

  List<CommentEntity> findAllByUserId(String userId);
}
