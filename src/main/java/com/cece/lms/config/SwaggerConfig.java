package com.cece.lms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI lmsOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Loan Management API")
                        .description("API documentation for Loan Management Module")
                        .version("v1.0"));
    }
}
