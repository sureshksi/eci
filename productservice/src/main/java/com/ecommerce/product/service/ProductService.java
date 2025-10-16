package com.ecommerce.product.service;

import java.util.List;

import com.ecommerce.product.entity.Product;
import com.ecommerce.product.exception.ProductException;

/**Product Service class
 * 
 * @author Suresh Injeti
 *
 */
public interface ProductService {
	
	public List<Product> getAllProducts() throws ProductException;
	public Product getProductById(int productId) throws ProductException;
	public void createProduct(Product product) throws ProductException;
	public void updateProduct(Product product) throws ProductException;
	public void deleteProduct(int productId) throws ProductException;
	public List<String> getCategories();
	public List<Product> getProductsByCategory(String category) throws ProductException;
	public List<Product> searchProducts(String productName) throws ProductException;

}
