package in.newdevpoint.bootcamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EnableAsync
@SpringBootApplication
// @EnableScheduling // Enables scheduling in the Spring application

@EnableGlobalMethodSecurity(prePostEnabled = true)
// @EnableEurekaClient
public class SBBootCampApplication {
private static final Logger logger = LoggerFactory.getLogger(SBBootCampApplication.class);
  public static void main(String[] args) {
    SpringApplication.run(SBBootCampApplication.class, args);

     logger.debug("DEBUG: Application started");
        logger.info("INFO: Application started");
        logger.warn("WARN: Application started");
        logger.error("ERROR: Application started");
  }
}
