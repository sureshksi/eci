package com.ecommerce.order.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**Client beans are creates
 * 
 * @author Suresh Injeti
 *
 */
@Configuration
public class RestClientConfig {
	@Value("${app.rest.url}")
    private String apiServiceUrl;

	//Inventory client bean
    @Bean
    InventoryClient inventoryClient() {	
        RestClient restClient = RestClient.builder()
                .baseUrl(apiServiceUrl)
                .requestFactory(getClientHttpRequestFactory())
                .build();
        
        RestClientAdapter restClientAdapter 		    = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        
        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }

    //Notification client bean
    @Bean
    NotificationClient notificationClient() {	
        RestClient restClient = RestClient.builder()
                .baseUrl(apiServiceUrl)
                .requestFactory(getClientHttpRequestFactory())
                .build();
        
        RestClientAdapter restClientAdapter 		    = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        
        return httpServiceProxyFactory.createClient(NotificationClient.class);
    }
    
    //Customer client bean
    @Bean
    CustomerClient customerClient() {	
        RestClient restClient = RestClient.builder()
                .baseUrl(apiServiceUrl)
                .requestFactory(getClientHttpRequestFactory())
                .build();
        
        RestClientAdapter restClientAdapter 		    = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        
        return httpServiceProxyFactory.createClient(CustomerClient.class);
    }
    
    //Http client connect timeout
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        // Set connection timeout to 3 seconds
        clientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(3)); 
        // Set read timeout to 5 seconds
        clientHttpRequestFactory.setReadTimeout(Duration.ofSeconds(5)); 
        return clientHttpRequestFactory;
    }
}