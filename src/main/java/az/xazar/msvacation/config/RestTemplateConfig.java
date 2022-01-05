package az.xazar.msvacation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public static RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

