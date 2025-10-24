package com.ecommerce.customer.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.customer.entity.Customer;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**Customer Service Rest controller
 * 
 * @author Suresh Injeti
 *
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

	@Autowired
	CustomerService customerService;
	
		@PutMapping
		public ResponseEntity<Object> updateCustomer(@Valid @RequestBody Customer customer, BindingResult bindingResult) {
			if (bindingResult.hasErrors()) {
				return ResponseEntity.badRequest().body("Validation failed");
			}
			try {
				customerService.updateCustomer(customer);
				return new ResponseEntity<Object>(customer, HttpStatus.ACCEPTED);
			} catch (CustomerException pe) {
				log.error("Failed to update Customer");
				return new ResponseEntity<Object>(pe.getMessage(), HttpStatus.NOT_FOUND);
			}
		}
		
		
		@GetMapping("/{customerId}")
		public ResponseEntity<Object> getCustomerById(@Valid @PathVariable Integer customerId) {
			try {
				Customer customer = customerService.getCustoemrById(customerId);
			return new ResponseEntity<Object>(customer, HttpStatus.OK);

			}catch(CustomerException pe) {
				log.error("Failed to get Customer details");
				return new ResponseEntity<Object>(pe.getMessage(),  HttpStatus.NOT_FOUND);
			}
		}
		
		
		@GetMapping
		public ResponseEntity<Object> getAllCustomers() {
			try {
			List<Customer> custoemrList =	customerService.getAllCustoemrs();
			return new ResponseEntity<Object>(custoemrList, HttpStatus.OK);
			}catch(CustomerException pe) {
				log.error("Failed to get all users");
				return new ResponseEntity<Object>(pe.getMessage(),  HttpStatus.NOT_FOUND);
			}

		}

		//create Customer
		@PostMapping
		public ResponseEntity<Object> createCustomer(@Valid @RequestBody Customer customer, BindingResult bindingResult) {
			if (bindingResult.hasErrors()) {
				return ResponseEntity.badRequest().body("Validation failed");
			}
			try {
				customerService.updateCustomer(customer);
				return new ResponseEntity<Object>(customer, HttpStatus.ACCEPTED);
			} catch (CustomerException pe) {
				return new ResponseEntity<Object>(pe.getMessage(), HttpStatus.NOT_FOUND);
			}
			
		}
		//Delete Customer
		@DeleteMapping("/{id}")
		public ResponseEntity<String> deleteCustomerById(@Valid @PathVariable("id") Integer custoemrId) {
			try {
				customerService.deleteCustomer(custoemrId);
			return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);

			}catch(CustomerException pe) {
				log.error("Failed to delete Customer");
				return new ResponseEntity<String>(pe.getMessage(),  HttpStatus.NOT_FOUND);
			}
		}
}
