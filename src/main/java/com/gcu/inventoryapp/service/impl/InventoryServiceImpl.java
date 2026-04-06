package com.gcu.inventoryapp.service.impl;

import com.gcu.inventoryapp.model.Inventory;
import com.gcu.inventoryapp.repository.InventoryRepository;
import com.gcu.inventoryapp.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<Inventory> getAllInventory() {
        logger.info("Entering getAllInventory()");
        List<Inventory> inventoryList = inventoryRepository.findAll();
        logger.info("Exiting getAllInventory() with {} records", inventoryList.size());
        return inventoryList;
    }

    @Override
    public Optional<Inventory> getInventoryById(Long id) {
        logger.info("Entering getInventoryById() with id={}", id);

        Optional<Inventory> inventory = inventoryRepository.findById(id);

        if (inventory.isPresent()) {
            logger.info("Inventory found for id={}", id);
        } else {
            logger.warn("Inventory NOT found for id={}", id);
        }

        logger.info("Exiting getInventoryById()");
        return inventory;
    }

    @Override
    public Inventory saveInventory(Inventory inventory) {
        logger.info("Entering saveInventory() for productId={}, quantity={}",
                inventory.getProduct() != null ? inventory.getProduct().getId() : null,
                inventory.getQuantity());

        Inventory savedInventory = inventoryRepository.save(inventory);

        logger.info("Exiting saveInventory() with id={}", savedInventory.getId());
        return savedInventory;
    }

    @Override
    public List<Inventory> getLowStock(int threshold) {
        logger.info("Entering getLowStock() with threshold={}", threshold);

        List<Inventory> lowStockItems = inventoryRepository.findByQuantityLessThan(threshold);

        logger.info("Exiting getLowStock() with {} low stock items", lowStockItems.size());
        return lowStockItems;
    }

    @Override
    public Optional<Inventory> getInventoryByProductId(Long productId) {
        logger.info("Entering getInventoryByProductId() with productId={}", productId);

        Optional<Inventory> inventory = inventoryRepository.findByProductId(productId);

        if (inventory.isPresent()) {
            logger.info("Inventory found for productId={}", productId);
        } else {
            logger.warn("No inventory found for productId={}", productId);
        }

        logger.info("Exiting getInventoryByProductId()");
        return inventory;
    }
}