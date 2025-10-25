package com.ecommerce.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**Product Service API entry class
 * 
 * @author Suresh Injeti
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProductServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApp.class, args);
	}

}
