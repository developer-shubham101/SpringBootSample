package com.example.apigateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

  @Bean
  public GlobalFilter logFilter() {
    return (exchange, chain) -> {
      System.out.println("Incoming request: " + exchange.getRequest().getPath());
      return chain
          .filter(exchange)
          .then(
              Mono.fromRunnable(
                  () ->
                      System.out.println(
                          "Outgoing response: " + exchange.getResponse().getStatusCode())));
    };
  }
}
