package com.ecommerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**Order Service API entry class
 * 
 * @author Suresh Injeti
 *
 */
@SpringBootApplication
//@EnableFeignClients
//@EnableDiscoveryClient
public class OrderServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApp.class, args);
	}
}
