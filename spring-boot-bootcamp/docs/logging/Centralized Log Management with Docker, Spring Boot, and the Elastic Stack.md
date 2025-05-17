**Log Management with Elasticsearch, Kibana, and Filebeat (Dockerized Spring Boot)**

This document outlines the setup and configuration for collecting, visualizing, and analyzing logs from a Dockerized Spring Boot application using the Elastic Stack (Elasticsearch, Kibana, Filebeat).

**1. Overview of Components:**

*   **Spring Boot Application:** Our application generating logs.
*   **Elasticsearch:** A distributed, RESTful search and analytics engine capable of storing and searching large volumes of log data.
*   **Kibana:** A data visualization and exploration tool used to analyze the logs stored in Elasticsearch through interactive dashboards and visualizations.
*   **Filebeat:** A lightweight shipper that collects log files or streams and forwards them to Elasticsearch.
*   **Docker:** A platform for running applications in isolated containers.
*   **Docker Compose:** A tool for defining and managing multi-container Docker applications.

**2. Docker Compose Configuration (`docker-compose.yml`):**

This file defines and manages the services within our log management setup.

 ```yaml
 version: '3.8'

 services:
   mongo:
     # ... (MongoDB service definition - included for context)

   springboot-app:
     build:
       context: .
       dockerfile: Dockerfile
     depends_on:
       - mongo
     environment:
       SPRING_DATA_MONGODB_URI: mongodb://mongo:27017/my_first_spring_project
     ports:
       - "9000:9000"
     networks:
       - springboot-mongo-network
     volumes:
       - ./logs:/logs  # Mount logs directory

   elasticsearch:
     image: docker.elastic.co/elasticsearch/elasticsearch:8.11.1
     environment:
       - discovery.type=single-node
       - xpack.security.enabled=false
     ports:
       - "9200:9200"
     volumes:
       - esdata:/usr/share/elasticsearch/data
     networks:
       - springboot-mongo-network

   kibana:
     image: docker.elastic.co/kibana/kibana:8.11.1
     ports:
       - "5601:5601"
     environment:
       - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
     depends_on:
       - elasticsearch
     networks:
       - springboot-mongo-network

   filebeat:
     build:
       context: . # Or the directory containing your Dockerfile.filebeat and filebeat.yml
       dockerfile: Dockerfile.filebeat
     user: root
     volumes:
       - ./logs:/usr/share/filebeat/logs:ro
       - ./filebeat.yml:/usr/share/filebeat/filebeat.yml:ro # Or rely on built-in from Dockerfile
       - /var/lib/docker/containers:/var/lib/docker/containers:ro
       - /var/run/docker.sock:/var/run/docker.sock
     depends_on:
       - elasticsearch
       - springboot-app
     networks:
       - springboot-mongo-network

 volumes:
   mongo-data:
   esdata:

 networks:
   springboot-mongo-network:
 ```

**Key Points:**

*   **`springboot-app` Volume:** Mounts the `./logs` directory from the host to `/logs` inside the container, where Spring Boot writes logs.
*   **`elasticsearch`:** Runs Elasticsearch on port 9200. Single-node discovery is configured (not for production). Security is disabled (not for production). Data is persisted in the `esdata` volume.
*   **`kibana`:** Runs Kibana on port 5601 and is configured to connect to Elasticsearch.
*   **`filebeat`:**
    *   **Build Configuration:** Uses a custom `Dockerfile.filebeat` to set file permissions.
    *   **Volumes:**
        *   Mounts the host's `./logs` directory to read Spring Boot logs.
        *   Mounts the `filebeat.yml` configuration file (can be built into the image instead).
        *   Mounts Docker socket and container information for potential auto-discovery (if configured in `filebeat.yml`).
    *   **Dependencies:** Starts after Elasticsearch and the Spring Boot application.

**3. Filebeat Configuration (`filebeat.yml`):**

This file configures how Filebeat collects and ships logs.

 ```yaml
 filebeat.inputs:
   - type: log
     enabled: true
     paths:
       - /usr/share/filebeat/logs/*.log # Adjust to your log file pattern

 output.elasticsearch:
   hosts: ["elasticsearch:9200"]
   # username: "elastic" # Uncomment if security is enabled
   # password: "your_password"

 # Optional processors (recommended)
 processors:
   - add_host_metadata:
   - add_cloud_metadata:
   # ... other processors ...

 # Optional Kibana setup
 setup.kibana:
   host: "kibana:5601"
 ```

**Key Points:**

*   **`filebeat.inputs`:** Defines where Filebeat looks for logs. The `paths` should match the location where your Spring Boot application writes logs inside the Filebeat container (which is mounted from `./logs` on the host).
*   **`output.elasticsearch`:** Specifies the Elasticsearch hosts to send data to.
*   **`processors`:** Allow you to enrich and modify log data before sending it to Elasticsearch.
*   **`setup.kibana`:** Configures connection to Kibana for index pattern setup.

**4. Filebeat Dockerfile (`Dockerfile.filebeat`):**

This Dockerfile ensures the `filebeat.yml` file has the correct permissions.

 ```dockerfile
 FROM docker.elastic.co/beats/filebeat:8.11.1
 USER root
 COPY filebeat.yml /usr/share/filebeat/filebeat.yml
 RUN chmod go-w /usr/share/filebeat/filebeat.yml
 # Optional: USER filebeat
 ```

**Key Points:**

*   Uses the official Filebeat image.
*   Copies the `filebeat.yml` configuration into the container.
*   Sets the permissions of `filebeat.yml` to be writable only by the owner, which is a security requirement for Filebeat.

**5. Spring Boot Logging Configuration (`application.properties` or `application.yml`):**

This configures how Spring Boot generates logs and where they are written.

**`application.properties`:**

 ```properties
 logging.level.root=INFO
 logging.level.org.springframework.web=DEBUG
 logging.level.in.newdevpoint.bootcamp=DEBUG
 logging.file.name=logs/app.log
 ```

**`application.yml`:**

 ```yaml
 logging:
   level:
     root: INFO
     org:
       springframework:
         web: DEBUG
     in:
       newdevpoint:
         bootcamp: DEBUG
   file:
     name: logs/app.log
 ```

**Key Points:**

*   **`logging.level.root`:** Sets the base logging level for the entire application.
*   **`logging.level.<package>`:** Sets specific logging levels for individual packages.
*   **`logging.file.name`:** Configures Spring Boot to write logs to a file named `app.log` in the `logs` directory relative to the application's working directory (which, due to the volume mount, ends up in `./logs` on the host).

**6. Running the Setup:**

1.  Ensure you have Docker and Docker Compose installed.
2.  Place all the configuration files (`docker-compose.yml`, `Dockerfile.filebeat`, `filebeat.yml`, and your Spring Boot application files including `application.properties`/`yml` and `Dockerfile`) in appropriate locations.
3.  Build and start the services using: `docker-compose up -d`

**7. Accessing Logs in Kibana:**

1.  Open Kibana in your browser (usually `http://localhost:5601`).
2.  Configure an index pattern (e.g., `filebeat-*`) in Kibana to match the indices created by Filebeat in Elasticsearch.
3.  Use the "Discover" section in Kibana to search, filter, and analyze your Spring Boot application logs.

**8. Key Considerations for Interviews and Implementation:**

*   **Log Levels:** Understand the different log levels (TRACE, DEBUG, INFO, WARN, ERROR, FATAL/OFF) and when to use them.
*   **Centralized Logging:** Explain the benefits of centralized logging (easier troubleshooting, analysis across multiple services, monitoring).
*   **Elastic Stack Components:** Be able to describe the role of each component (Elasticsearch for storage and search, Kibana for visualization, Filebeat for shipping).
*   **Dockerization:** Understand how Docker simplifies deployment and management of these components.
*   **Volume Mounts:** Explain the purpose of volume mounts for persisting data and sharing logs between the host and containers.
*   **Filebeat Configuration:** Know how to configure Filebeat inputs, outputs, and processors.
*   **Spring Boot Logging:** Understand how to configure logging levels and file output in Spring Boot using `application.properties` or `application.yml`.
*   **Troubleshooting:** Be prepared to discuss common issues and how to troubleshoot them (e.g., connectivity problems, configuration errors, permission issues).
*   **Production Considerations:** Highlight that the provided `docker-compose.yml` is suitable for development but would need significant modifications for production (e.g., enabling Elasticsearch security, configuring cluster nodes, persistent storage, proper resource management).

This document provides a comprehensive overview of the log management setup. Remember to tailor it further based on the specific requirements and details of your project or interview context.