package com.ecommerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.openfeign.EnableFeignClients;

/**Order Service API entry class
 * 
 * @author Suresh Injeti
 *
 */
@SpringBootApplication
//@EnableFeignClients
@EnableDiscoveryClient
public class OrderService {

	public static void main(String[] args) {
		SpringApplication.run(OrderService.class, args);
	}

}
