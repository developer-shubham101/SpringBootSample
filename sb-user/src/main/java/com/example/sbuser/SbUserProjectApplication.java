package com.example.sbuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SbUserProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(SbUserProjectApplication.class, args);
  }
}
