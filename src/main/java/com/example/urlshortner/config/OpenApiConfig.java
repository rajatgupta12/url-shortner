package com.example.urlshortner.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener API")
                        .version("v0.0.1")
                        .description("API for creating and resolving shortened URLs")
                        .contact(new Contact().name("Rajat Gupta").email("gupta28rajat@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                .components(new Components().addSecuritySchemes("bearer-key", getSecurityScheme()));
    }

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
                .bearerFormat("JWT")
                .scheme("bearer")
                .type(SecurityScheme.Type.HTTP);
    }
}

