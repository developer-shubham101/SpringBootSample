package com.example.sbuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableAsync
@SpringBootApplication
// @EnableScheduling // Enables scheduling in the Spring application

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SbUserProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(SbUserProjectApplication.class, args);
  }
}
