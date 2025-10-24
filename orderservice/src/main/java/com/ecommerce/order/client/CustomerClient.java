package com.ecommerce.order.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import com.ecommerce.order.pojo.Customer;

/**Customer client call micro service
 * 
 * @author Suresh Injeti
 *
 */
public interface CustomerClient {

	@GetExchange("/api/v1/customers")
	public Customer getCustomerById(@PathVariable Integer customerId);
	
    default boolean customerFallback(Customer user, Throwable ex) {
       // log.info(" Get customer fallback triggered: " + ex.getMessage());
        return false;
    }
}
