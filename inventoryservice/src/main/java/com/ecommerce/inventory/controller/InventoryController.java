package com.ecommerce.inventory.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.exception.InventoryException;
import com.ecommerce.inventory.service.InventoryService;
import com.ecommerce.inventory.pojo.IdempotencyResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**Inventory Service Rest controller
 * 
 * @author Suresh Injeti
 *
 */
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {
	
	@Autowired
	InventoryService inventoryService;

	//To submit multiple requests will same response
	public static HashMap<String, IdempotencyResponse> idempotencyKeyMap = new HashMap<>();

	//Update product details
	@PutMapping
	public ResponseEntity<?> updateInventoryProduct(@Valid @RequestBody Inventory inventory, BindingResult bindingResult) {
		log.info("Inventory updation starts");
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body("Validation failed");
		}
		try {
			inventoryService.updateInventory(inventory);
			log.error("Failed to update Inventory");
			return new ResponseEntity<>(inventory, HttpStatus.ACCEPTED);
		} catch (InventoryException pe) {
			log.error("Failed to update Inventory");
			return new ResponseEntity<>(pe.getMessage(), HttpStatus.NOT_FOUND);
		}

	}
	//Get All inventory list
	@GetMapping
	public ResponseEntity<?> getAllInventoryList() {
		try {
			List<Inventory> inventory = inventoryService.getAllProducts();
		return new ResponseEntity<>(inventory, HttpStatus.OK);

		}catch(Exception pe) {
			log.error("Failed to get product details");
			return new ResponseEntity<>(pe.getMessage(),  HttpStatus.NOT_FOUND);
		}
	}
	//Get product inventory details
	@GetMapping("/product/{productId}")
	public ResponseEntity<?> getInventoryProductById(@Valid @PathVariable Integer productId) {
		try {
			Inventory inventory = inventoryService.getProductById(productId);
		return new ResponseEntity<>(inventory, HttpStatus.OK);

		}catch(InventoryException pe) {
			log.error("Failed to get product details");
			return new ResponseEntity<>(pe.getMessage(),  HttpStatus.NOT_FOUND);
		}
	}
	
	//Reserve product in inventory
	@PutMapping("/product/reserve")
	public ResponseEntity<?> reserveProducct(@Valid @RequestParam int productId, @RequestParam int quantity) {
		try {
			Boolean reserve = inventoryService.reserveByProduct(productId, quantity);
		return new ResponseEntity<>(reserve, HttpStatus.OK);
		}catch(InventoryException pe) {
			log.error("Failed to reserve product");
			return new ResponseEntity<>(false,  HttpStatus.NOT_FOUND);
		}

	}
	//Release reserved product in inventory
	@PutMapping("/product/release")
	public ResponseEntity<?> releaseProducct(@Valid @RequestParam int productId, @RequestParam boolean relaseType) {
		try {
		  Boolean release = inventoryService.releaseProducct(productId, relaseType);
		return new ResponseEntity<>(release, HttpStatus.OK);
		}catch(InventoryException pe) {
			log.error("Failed to release product reservation");
			return new ResponseEntity<>(false,  HttpStatus.NOT_FOUND);
		}

	}
	//create inventory
	@PostMapping
	public ResponseEntity<?> createInventoryProduct(@Valid @RequestBody Inventory inventory, @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey, BindingResult bindingResult) {
		log.info("Inventory createtion started");
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body("Validation failed");
		}
		if (idempotencyKey == null || idempotencyKey.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("Missing Product Idempotency-Key header");
	    }
	    
	    if (idempotencyKeyMap.containsKey(idempotencyKey)) {
	    	IdempotencyResponse response = idempotencyKeyMap.get(idempotencyKey);
	    	
	        log.info("Returning cached Product response for Idempotency-Key: {}", idempotencyKey);
	        return new ResponseEntity<>(response.getResponseBody(), response.getResponseStatus());
	    }
	    IdempotencyResponse idemtepotenyRes = new IdempotencyResponse();
		try {
			inventoryService.createInventory(inventory);
			idemtepotenyRes.setResponseStatus(HttpStatus.CREATED);
			idemtepotenyRes.setResponseBody(inventory.toString());
			log.info("Inventory createtion ends");

		}catch(InventoryException pe) {
			idemtepotenyRes.setResponseStatus(HttpStatus.NOT_FOUND);
			idemtepotenyRes.setResponseBody(pe.getMessage());
			log.error("Failed to create Inventory");
		}
		return new ResponseEntity<>(idemtepotenyRes.getResponseBody(), idemtepotenyRes.getResponseStatus());
	
	}
	//Delete inventory
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrderById(@Valid @PathVariable("id") Integer inventoryId) {
		try {
			inventoryService.deleteInventory(inventoryId);
		return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);

		}catch(InventoryException pe) {
			log.error("Failed to delete inventory");
			return new ResponseEntity<>(pe.getMessage(),  HttpStatus.NOT_FOUND);
		}
	}
	
    @GetMapping("/product/instock")
    public ResponseEntity<?> isInStock(@RequestParam int productId, @RequestParam int quantity) {
    	
        boolean inStock = inventoryService.isInStock(productId, quantity);
        return new ResponseEntity<>(inStock, HttpStatus.OK);
    }

}
