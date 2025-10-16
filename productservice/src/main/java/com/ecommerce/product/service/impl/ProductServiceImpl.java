package com.ecommerce.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.product.entity.Product;
import com.ecommerce.product.exception.ProductException;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**Product Service  class
 * 
 * @author Suresh Injeti
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private  ProductRepository productRepository;

	@Override
	public List<Product> getAllProducts() throws ProductException{
		List<Product> productList =  productRepository.findAll();
		if(productList == null || productList.isEmpty())
			throw new ProductException("No products available");
		else
			return productList;
	}

	@Transactional
	@Override
	public void createProduct(Product product) throws ProductException{
		try {
			productRepository.save(product);
			log.info("Product {} is created successfully", product.getProductId());
		}catch(Exception e) {
			log.error("Product creation failed");
			throw e;
		}
	}

	@Override
	public Product getProductById(int productId) throws ProductException{
		return productRepository.findById(productId).get();		
	}

	@Transactional
	@Override
	public void updateProduct(Product product) throws ProductException{
		try {
			productRepository.save(product);
			log.info("Product {} is updated successfully", product.getProductId());
		}catch(Exception e) {
			log.error("Product updation failed");
			throw e;
		}
	}

	@Transactional
	@Override
	public void deleteProduct(int productId) throws ProductException{
		try {
			productRepository.deleteById(productId);
			log.info("Product {} is deleted successfully", productId);
		}catch(Exception e) {
			log.error("Product deletion failed");
			throw e;
		}
	}

	@Override
	public List<String> getCategories() {
		List<String> categoryList =	productRepository.getCategories();
		
		if(categoryList != null && !categoryList.isEmpty())
			return categoryList;
		else
			return null;
	}

	@Override
	public List<Product> getProductsByCategory(String category) throws ProductException{
		
		List<Product> productList = productRepository.findByCategory(category);
		if(productList != null && !productList.isEmpty()) {
			return productList;
		}else {
			throw new ProductException("Product matach found with '"+category+"' category");
		}
		
	}

	@Override
	public List<Product> searchProducts(String productName) throws ProductException {
		
		List<Product> productList = productRepository.searchByName(productName);
		if(productList != null && !productList.isEmpty()) {
			return productList;
		}else {
			throw new ProductException("Product matach found with search '"+productName+"' ");
		}
		
	}
	
	
}
