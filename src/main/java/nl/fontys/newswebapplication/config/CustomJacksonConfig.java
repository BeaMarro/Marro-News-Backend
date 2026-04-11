package nl.fontys.newswebapplication.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomJacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() { // Changes Json configuration to set date to a specific format
        return builder -> builder.simpleDateFormat("dd/MM/yyyy");
    }
}
