package com.example.firstproject.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

  private final Environment environment;

  public ProfileController(Environment environment) {
    this.environment = environment;
  }

  @GetMapping("/activeProfile")
  public String getActiveProfile() {
    String googleMapKey = environment.getProperty("google.map.key");
    String apiKey = environment.getProperty("API_KEY");
    return "Active profile: "
        + String.join(", ", environment.getActiveProfiles())
        + "\n"
        + googleMapKey
        + "\n"
        + apiKey;
  }
}
