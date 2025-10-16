package com.ecommerce.order.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.ecommerce.order.client.InventoryClient;
@Configuration
public class RestClientConfig {
	@Value("${inventory.url}")
    private String inventoryServiceUrl;

    @Bean
    InventoryClient inventoryClient() {	
        RestClient restClient = RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .requestFactory(getClientHttpRequestFactory())
                .build();
        
        RestClientAdapter restClientAdapter 		    = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        
        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        // Set connection timeout to 3 seconds
        clientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(3)); 
        // Set read timeout to 5 seconds
        clientHttpRequestFactory.setReadTimeout(Duration.ofSeconds(5)); 
        return clientHttpRequestFactory;
    }
}