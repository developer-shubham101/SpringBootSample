package com.example.sbblogcomment.repository;

import com.example.sbblogcomment.entity.ExceptionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExceptionLogRepository extends MongoRepository<ExceptionLog, String> {}
