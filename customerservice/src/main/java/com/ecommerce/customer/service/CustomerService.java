package com.ecommerce.customer.service;

import java.util.List;

import com.ecommerce.customer.entity.Customer;
import com.ecommerce.customer.exception.CustomerException;

public interface CustomerService {
	
	public Customer getCustoemrById(Integer id) throws CustomerException;
	public List<Customer> getAllCustoemrs() throws CustomerException;
	public Customer addCustomer(Customer customer) throws CustomerException;
	public Customer updateCustomer(Customer customer) throws CustomerException;
	public void deleteCustomer(Integer id) throws CustomerException;
	

}
