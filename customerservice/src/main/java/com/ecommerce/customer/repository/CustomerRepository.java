package com.ecommerce.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.customer.entity.Customer;

/**Customer repository for REST services
 * 
 * @author Suresh Injeti
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
