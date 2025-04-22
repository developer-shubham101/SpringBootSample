package in.newdevpoint.bootcamp.config;

import com.mongodb.client.MongoClient;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationShutdown {

  @Autowired private MongoClient mongoClient;

  /**
   * Closes the MongoDB client connection during application shutdown.
   *
   * <p>Invoked automatically before the bean is destroyed to ensure all MongoDB connections are
   * properly closed.
   */
  @PreDestroy
  public void cleanUp() {
    if (mongoClient != null) {
      mongoClient.close(); // Close all MongoDB connections on shutdown
    }
  }
}
