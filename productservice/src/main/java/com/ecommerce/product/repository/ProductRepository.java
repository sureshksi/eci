package com.ecommerce.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.product.entity.Product;

/**Product Service Repository
 * 
 * @author Suresh Injeti
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	/**
	 * To get the product list, categories
	 * 
	 * @param category
	 * @return Optional<Product>
	 */
	@Query("SELECT pc FROM Product pc WHERE LOWER(pc.category) = LOWER(:category) AND pc.is_active='True'")
	List<Product> findByCategory(@Param("category") String category);
	
	/**
	 * To get the all product  categories list
	 * 
	 * @return Optional<String>
	 */
	@Query("SELECT distinct(p.category) FROM Product p WHERE p.is_active='True'")
	List<String> getCategories();

	
	@Query("SELECT pc FROM Product pc WHERE pc.name LIKE %:name%")
	List<Product> searchByName(@Param("name") String name);
	
}
