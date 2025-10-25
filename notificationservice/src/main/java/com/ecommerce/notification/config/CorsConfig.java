package com.ecommerce.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**CORS configuration for REST services
 * 
 * @author Suresh Injeti
 *
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}




