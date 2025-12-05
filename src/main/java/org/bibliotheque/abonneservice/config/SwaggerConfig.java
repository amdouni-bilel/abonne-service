package org.bibliotheque.abonneservice.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("spring-boot-api")
                .pathsToMatch("/**") // Définit les chemins d'API à inclure dans Swagger
                .build();
    }
}
