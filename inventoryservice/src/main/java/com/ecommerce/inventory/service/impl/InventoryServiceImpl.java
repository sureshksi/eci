package com.ecommerce.inventory.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.exception.InventoryException;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.service.InventoryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**Inventory Service class
 * 
 * @author Suresh Injeti
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	private  InventoryRepository inventoryRepository;

	@Override
	public Inventory getProductById(int productId) throws InventoryException {
		Optional<Inventory> order = inventoryRepository.getProductById(productId);
		if (order.isEmpty())
			throw new InventoryException("Product not found in Inventory with '" + productId + "' ");
		else
			return order.get();
	}
	
	@Transactional
	@Override
	public void reserveByProduct(int productId, int quantity) throws InventoryException {
		Inventory inventory = this.getProductById(productId);
		if(inventory!=null && inventory.getReserved()==0) {
			inventoryRepository.reservedByProductId(productId, (inventory.getOnHand() - quantity), quantity, LocalDateTime.now());
			log.info("Product {} is reserved in Inventory successfully", productId);
		}else {
			log.info("Product already reserved in Inventory", productId);
			//throw new InventoryException("Product already reserved");
		}
		
	}

	@Transactional
	@Override
	public void releaseProducct(int productId) throws InventoryException {
		Inventory inventory = this.getProductById(productId);
		if(inventory!=null && inventory.getReserved()!=0) {
			inventoryRepository.reservedByProductId(productId, (inventory.getOnHand()+inventory.getReserved()), 0, LocalDateTime.now());
			log.info("Product {} is released from reservation in Inventory successfully", productId);
		}else {
			log.info("Product already released in Inventory", productId);
			//throw new InventoryException("Product already released");			
		}	
	}

	@Transactional
	@Override
	public void updateInventory(Inventory inventory) throws InventoryException {
		try {
			inventoryRepository.save(inventory);
			log.info("Inventory {} is updated successfully", inventory.getInventoryId());
		}catch(Exception e) {
			log.error("Inventory updation failed");
			throw e;
		}		
	}

	@Transactional
	@Override
	public void createInventory(Inventory inventory) throws InventoryException {
		try {
			inventoryRepository.save(inventory);
			log.info("Inventory {} is created \successfully", inventory.getInventoryId());
		}catch(Exception e) {
			log.error("Inventory creation failed");
			throw e;
		}		
		
	}

	@Override
	public void deleteInventory(int inventoryId) throws InventoryException {
		try {
			inventoryRepository.deleteById(inventoryId);
			log.info("Inventory {} is deleted successfully", inventoryId);
		} catch (Exception e) {
			log.error("Inventory  deletion failed");
			throw e;
		}
		
	}
	
	@Override
	@Transactional
    public boolean isInStock(int productId, int quantity) {
        return inventoryRepository.existsByProductIdAndOnHandGreaterThanEqual(productId, quantity);
    }
	
    // Runs every 5 minutes
    @Scheduled(fixedRate = 300000) // 300000ms = 5 minutes
    @Transactional
    public void releaseExpiredReservations() {
    	log.info("Release expired reservations started");
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(15);
        inventoryRepository.releaseExpiredReservations(cutoffTime);
        log.info("Release expired reservations ended");
    }
}
