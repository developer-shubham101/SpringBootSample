package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableEurekaClient
public class ApiGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(com.example.apigateway.ApiGatewayApplication.class, args);
  }
}
