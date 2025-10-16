package com.ecommerce.product.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product.entity.Product;
import com.ecommerce.product.exception.ProductException;
import com.ecommerce.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**Product Service Rest controller
 * 
 * @author Suresh Injeti
 *
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
	
	@Autowired
	ProductService productService;
	

	@PostMapping
	public ResponseEntity<Object> createProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
		log.info("product createtion started");
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body("Validation failed");
		}
		try {
		 productService.createProduct (product);
		 log.info("product createtion ends");
		 return new ResponseEntity<Object>(product, HttpStatus.CREATED);

		}catch(ProductException pe) {
			log.error("Failed to create product");
			return new ResponseEntity<Object>("Failed to create product",  HttpStatus.NOT_FOUND);
		}
		
	}

//	// This method gets the product which needs to get product details by id
//	@GetMapping("/products/{id}")
//	public ResponseEntity<Object> getProductById(@Valid @PathVariable("id") Integer productId) {
//
//		try {
//		Product prod = productService.getProductById(productId);
//
//		return new ResponseEntity<Object>(prod, HttpStatus.FOUND);
//		}catch(ProductException pe) {
//			log.error("Failed to create product");
//			return new ResponseEntity<Object>(pe.getMessage(),  HttpStatus.NOT_FOUND);
//		}
//
//	}

	// This method will delete the product from list
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProductById(@Valid @PathVariable("id") Integer productId) {
		try {
		productService.deleteProduct(productId);
		return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);

		}catch(ProductException pe) {
			log.error("Failed to create product");
			return new ResponseEntity<String>(pe.getMessage(),  HttpStatus.NOT_FOUND);
		}
	}

	// Update product details
	@PutMapping
	public ResponseEntity<Object> updateProduct(@Valid @RequestBody Product prod, BindingResult bindingResult) {
		log.info("Product updation started");
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body("Validation failed");
		}
		try {
			productService.updateProduct(prod);
			log.info("Product updation ends");
			return new ResponseEntity<Object>(prod, HttpStatus.ACCEPTED);
		} catch (ProductException pe) {
			log.error("Failed to update product");
			return new ResponseEntity<Object>(pe.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	//Get all list of products
	@GetMapping
	public ResponseEntity<Object> getAllProductsr(@RequestParam(name = "productNme", defaultValue="", required = true) String productName) {
		try {
			if (productName != null && !productName.isBlank()) {
				List<Product> list = productService.searchProducts(productName);
				return new ResponseEntity<Object>(list, HttpStatus.OK);
			} else {
				List<Product> list = productService.getAllProducts();
				return new ResponseEntity<Object>(list, HttpStatus.OK);
			}
		} catch (ProductException pe) {
			log.error("Failed to get products by product name");
			return new ResponseEntity<Object>(pe.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	//Get products list by category
	@GetMapping("/{category}")
	public ResponseEntity<Object> getProductsByCategory(@Valid @PathVariable String category) {
		try {
		List<Product> list = productService.getProductsByCategory(category);
		
		return new ResponseEntity<Object>(list, HttpStatus.OK);
		}catch(ProductException pe) {
			log.error("Failed to get products by category");
			return new ResponseEntity<Object>(pe.getMessage(),  HttpStatus.NOT_FOUND);
		}

	}

	//Get all Categories
	@GetMapping("/categories")
	public ResponseEntity<List<String>> getCategories() {
		List<String> list = productService.getCategories();
		return new ResponseEntity<List<String>>(list, HttpStatus.OK);

	}

}
