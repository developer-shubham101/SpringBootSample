package in.newdevpoint.bootcamp.config;

import com.mongodb.client.MongoClient;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class handles the graceful shutdown of application resources. It ensures that all database
 * connections and other resources are properly closed when the Spring Boot application is shutting
 * down.
 *
 * <p>The class is marked as a Spring component to be automatically detected and managed by the
 * Spring container.
 */
@Component
public class ApplicationShutdown {

  /**
   * MongoDB client instance that needs to be properly closed during shutdown. Autowired by Spring
   * to inject the configured MongoDB client.
   */
  @Autowired private MongoClient mongoClient;

  /**
   * This method is automatically called by Spring during application shutdown. It ensures proper
   * cleanup of MongoDB connections to prevent resource leaks.
   *
   * <p>The @PreDestroy annotation ensures this method is called before the bean is destroyed,
   * making it ideal for cleanup operations.
   *
   * <p>Key responsibilities: - Checks if MongoDB client exists - Closes all active MongoDB
   * connections - Prevents resource leaks during application shutdown
   */
  @PreDestroy
  public void cleanUp() {
    if (mongoClient != null) {
      mongoClient.close(); // Close all MongoDB connections on shutdown
    }
  }
}
