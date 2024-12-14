package com.example.sbuser.config;

import com.mongodb.client.MongoClient;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationShutdown {

  @Autowired private MongoClient mongoClient;

  @PreDestroy
  public void cleanUp() {
    if (mongoClient != null) {
      mongoClient.close(); // Close all MongoDB connections on shutdown
    }
  }
}
