package com.gcu.inventoryapp.service;

import com.gcu.inventoryapp.model.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<Inventory> getAllInventory();
    Optional<Inventory> getInventoryById(Long id);
    Inventory saveInventory(Inventory inventory);
    List<Inventory> getLowStock(int threshold);
    Optional<Inventory> getInventoryByProductId(Long productId);
}