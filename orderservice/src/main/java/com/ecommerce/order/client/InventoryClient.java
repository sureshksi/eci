package com.ecommerce.order.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PutExchange;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

/**Inventory micro service
 * 
 * @author Suresh Injeti
 *
 */
//@FeignClient(name = "inventory",  url = "${inventory.url}")
public interface InventoryClient {
	public static final String QUANTITY2 = "quantity";
	public static final String PRODUCT_ID = "productId";
	Logger log = LoggerFactory.getLogger(InventoryClient.class);
	
	@GetExchange("/api/v1/inventory/product/instock")
    @CircuitBreaker(name = "instock", fallbackMethod = "fallbackStockRCMethod")
    @Retry(name = "instock")
    Boolean isInStock(@RequestParam(PRODUCT_ID) Integer productId,
                      @RequestParam(QUANTITY2) Integer quantity);
    
	//@PutMapping("/api/v1/inventory/product/reserve/product")
	
	@PutExchange("/api/v1/inventory/product/reserve")
    @CircuitBreaker(name = "reserve", fallbackMethod = "fallbackRCMethod")
    @Retry(name = "reserve")
    Boolean reserveProducct(@RequestParam(PRODUCT_ID) Integer productId,
            @RequestParam(QUANTITY2) Integer quantity); ///reserve/product/{productId}
    
	//@PutMapping("/api/v1/inventory/product/release/product")
	@PutExchange("/api/v1/inventory/product/release")
    @CircuitBreaker(name = "reserve", fallbackMethod = "fallbackRCMethod")
    @Retry(name = "reserve")
    Boolean releaseProducct(@RequestParam(PRODUCT_ID) Integer productId, @RequestParam boolean relaseType);
    
    default boolean fallbackStockRCMethod( Throwable t) {
        log.error("Cannot get availability ofproductId ",t);
        return false;
    }
    default boolean fallbackRCMethod( Throwable t) {
        log.error("Cannot get reserve/release for productId ", t);
        return false;
    }

}


//org.springframework.web.client.HttpClientErrorException$MethodNotAllowed: 405 : "{"timestamp":"2025-10-16T18:11:17.901+00:00","status":405,"error":"Method Not Allowed","path":"/api/v1/inventory/product/release"}"