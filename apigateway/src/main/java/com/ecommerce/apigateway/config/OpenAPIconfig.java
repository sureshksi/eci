package com.ecommerce.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIconfig {

    @Bean
    OpenAPI productServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("API-GatewayService")
                        .description("This is the API Gateway Service")
                        .version("V1.0.0")
                        .license(new License().name("Apache 2.0")));
    }
}