package in.newdevpoint.bootcamp.config;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ApplicationShutdown {

    @Autowired
    private MongoClient mongoClient;

    @PreDestroy
    public void cleanUp() {
        if (mongoClient != null) {
            mongoClient.close(); // Close all MongoDB connections on shutdown
        }
    }
}
