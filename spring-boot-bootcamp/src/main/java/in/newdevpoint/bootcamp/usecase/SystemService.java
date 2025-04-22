package in.newdevpoint.bootcamp.usecase;

import in.newdevpoint.bootcamp.entity.CoffeeEntity;
import in.newdevpoint.bootcamp.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class SystemService {
    private static final Logger logger = LoggerFactory.getLogger(SystemService.class);

    @Autowired
    RestTemplate restTemplate;

    public Object fetchExternalApi() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        Map<String, String> params = new HashMap<>();
        //          params.put("key", "Value");

        String url = "https://api.sampleapis.com/coffee/hot";

        ResponseEntity<ArrayList<CoffeeEntity>> coffeeEntityResponseEntity =
                restTemplate.exchange(
                        url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
                        }, params);

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
            logger.error("Exception occurred while reading file", e);
        }
        return "File not found";
    }
}
