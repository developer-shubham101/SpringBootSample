package in.newdevpoint.bootcamp.repository;

import in.newdevpoint.bootcamp.entity.ExceptionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExceptionLogRepository extends MongoRepository<ExceptionLog, String> {}
