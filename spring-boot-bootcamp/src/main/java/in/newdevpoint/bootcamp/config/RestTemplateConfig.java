package in.newdevpoint.bootcamp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  //  @Autowired RestTemplateResponseErrorHandler handler;

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }
}
