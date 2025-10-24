package com.ecommerce.inventory.service;

import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.exception.InventoryException;

/**Inventory Service class
 * 
 * @author Suresh Injeti
 *
 */
public interface InventoryService {
	
	public Inventory getProductById(int productId) throws InventoryException;
	public void reserveByProduct(int productId, int quantity) throws InventoryException;
	public void releaseProducct(int productId) throws InventoryException;
	public void updateInventory(Inventory inventory) throws InventoryException;
	public void createInventory(Inventory inventory) throws InventoryException;
	public void deleteInventory(int inventoryId) throws InventoryException;
	public boolean isInStock(int productId, int quantity);
	
}
