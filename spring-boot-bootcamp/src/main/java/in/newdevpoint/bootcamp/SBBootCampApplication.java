package in.newdevpoint.bootcamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableAsync  // Enables asynchronous method execution using @Async annotation
@SpringBootApplication  // Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan
// @EnableScheduling  // Uncomment to enable scheduling capabilities
@EnableGlobalMethodSecurity(prePostEnabled = true)  // Enables method-level security using annotations
// @EnableEurekaClient  // Uncomment to enable service registration with Eureka
public class SBBootCampApplication {
    /**
     * Logger instance for this class.
     * Uses SLF4J for logging with Logback as the implementation.
     */
    private static final Logger logger = LoggerFactory.getLogger(SBBootCampApplication.class);

    /**
     * Main method that bootstraps the Spring Boot application.
     * This method:
     * 1. Creates the Spring application context
     * 2. Starts the embedded web server
     * 3. Initializes all Spring beans
     * 4. Demonstrates different logging levels
     * 
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        // Bootstrap the Spring application
        SpringApplication.run(SBBootCampApplication.class, args);

        // Demonstrate different logging levels
        // These logs help verify the logging configuration
        logger.debug("DEBUG: Application started - Detailed information for debugging");
        logger.info("INFO: Application started - General information about application startup");
        logger.warn("WARN: Application started - Warning messages for potential issues");
        logger.error("ERROR: Application started - Error messages for serious problems");
    }
}
