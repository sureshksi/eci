package com.ecommerce.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**OpenAPI documentation for REST services
 * 
 * @author Suresh Injeti
 *
 */
@Configuration
public class OpenAPIconfig {

    @Bean
    OpenAPI productServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("Customer Service API")
                        .description("This is the REST API for Customer Service")
                        .version("V1.0.0")
                        .license(new License().name("Apache 2.0")));
    }
}