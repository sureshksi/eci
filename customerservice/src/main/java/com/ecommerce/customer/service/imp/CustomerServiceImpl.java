package com.ecommerce.customer.service.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.customer.entity.Customer;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.repository.CustomerRepository;
import com.ecommerce.customer.service.CustomerService;

/**Customer service  for REST services
 * 
 * @author Suresh Injeti
 *
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	
	@Override
	public Customer getCustoemrById(Integer id) throws CustomerException {
	 Optional<Customer> customerOpt =	customerRepository.findById(id);
	 if(customerOpt.isEmpty())
		 throw new CustomerException("Customer details not available");
	 else
		 return customerOpt.get();
	}

	@Override
	public List<Customer> getAllCustoemrs() throws CustomerException{
		try {
			return customerRepository.findAll();
		}catch(Exception e) {
			throw e;
		}
	}

	@Override
	public Customer addCustomer(Customer customer) throws CustomerException {
		try {
			customerRepository.save(customer);
			return customer;
		}catch(Exception e) {
			throw new CustomerException("Could not add Customer");
		}
	}

	@Override
	public Customer updateCustomer(Customer customer) throws CustomerException {
		try {
		customerRepository.save(customer);
		return customer;
		}catch(Exception e) {
			throw new CustomerException("Could not update Customer");
		}
	}

	@Override
	public void deleteCustomer(Integer id) throws CustomerException {
		try {
			customerRepository.deleteById(id);
		}catch(Exception e) {
			throw new CustomerException("Could not delete Customer");
		}
		
	}

}
