package com.ecommerce.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF for API use
            //.cors(cors -> {}) // Let Spring Cloud Gateway handle CORS
            .authorizeExchange(
            		exchange -> exchange
            		//.pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/**").permitAll()
            		.anyExchange().permitAll() // Or secure routes as needed
            )
            .build();
    }
}
