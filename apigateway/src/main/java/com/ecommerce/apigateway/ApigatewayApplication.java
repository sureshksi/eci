package com.ecommerce.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**API-Gateway Service for all Micro services
 * 
 * @author Suresh Injeti
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApigatewayApplication.class, args);
	}

//	@Bean
//	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//	    return builder.routes()
//	        .route("product-service", r -> r.path("/api/v1/products/**").uri("http://product-service:8082"))
//	        .route("order-service", r -> r.path("/api/v1/orders/**").uri("http://order-service:8083"))
//	        .route("inventory-service", r -> r.path("/api/v1/inventory/**").uri("http://inventory-service:8084"))
//	        .build();
//	}

//	 @Bean
//	 RouteLocator routeLocator(RouteLocatorBuilder builder) {
//	  return builder
//	    .routes()
//	    .route(r -> r.path("/v3/products/api-docs").and().method(HttpMethod.GET).uri("http://product-service:8082"))
//	    .route(r -> r.path("/v3/orders/api-docs").and().method(HttpMethod.GET).uri("http://order-service:8083"))
//	    .route(r -> r.path("/v3/inventory/api-docs").and().method(HttpMethod.GET).uri("http://inventory-service:8084"))
////	    .route(r -> r.path("/api/v1/products/**").and().method(HttpMethod.GET).uri("http://localhost:8082"))
////	    .route(r -> r.path("/api/v1/orders/**").and().method(HttpMethod.GET).uri("http://localhost:8083"))
////	    .route(r -> r.path("/api/v1/inventory/**").and().method(HttpMethod.GET).uri("http://localhost:8084"))
//	    .build();
//	 }
}
