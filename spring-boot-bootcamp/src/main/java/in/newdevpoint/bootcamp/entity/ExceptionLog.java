package in.newdevpoint.bootcamp.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "exception_logs")
@Data
public class ExceptionLog {
  @Id private String id;
  private String exceptionMessage;
  private String stackTrace;
  private String methodName;
  private String className;
  private String timestamp;

  // Getters and setters
}
