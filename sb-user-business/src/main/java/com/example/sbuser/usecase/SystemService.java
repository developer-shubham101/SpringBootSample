package com.example.sbuser.usecase;

import com.example.sbuser.entity.CoffeeEntity;
import com.example.sbuser.utility.Utility;
import java.io.IOException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SystemService {

  @Autowired RestTemplate restTemplate;

  public Object fetchExternalApi() {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<?> requestEntity = new HttpEntity<>(headers);

    Map<String, String> params = new HashMap<>();
    //          params.put("key", "Value");

    String url = "https://api.sampleapis.com/coffee/hot";

    ResponseEntity<ArrayList<CoffeeEntity>> coffeeEntityResponseEntity =
        restTemplate.exchange(
            url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {}, params);

    /*ResponseEntity<String> coffeeEntityResponseEntity =
    restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, params);*/

    //    restTemplate.getForEntity(url, String.class);
    //    restTemplate.postForEntity(url, params, String.class);

    System.out.println("API response code:");
    System.out.println(coffeeEntityResponseEntity.getStatusCode());

    return coffeeEntityResponseEntity.getBody();
  }

  public String readFile() {
    ClassPathResource classPathResource = new ClassPathResource("res/sample.txt");
    try {
      return Utility.getTemplateString(classPathResource);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "File not found";
  }
}
