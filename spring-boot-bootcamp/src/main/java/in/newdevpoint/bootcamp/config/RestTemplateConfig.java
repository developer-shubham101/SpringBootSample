package in.newdevpoint.bootcamp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    /**
     * Creates and provides a new {@link RestTemplate} bean for performing HTTP requests.
     *
     * @return a new instance of RestTemplate
     */

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
