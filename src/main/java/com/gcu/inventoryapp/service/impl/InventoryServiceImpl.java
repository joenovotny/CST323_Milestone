package com.gcu.inventoryapp.service.impl;

import com.gcu.inventoryapp.model.Inventory;
import com.gcu.inventoryapp.repository.InventoryRepository;
import com.gcu.inventoryapp.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation responsible for handling inventory-related business logic.
 * Provides operations for retrieving, saving, and analyzing inventory data.
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;

    /**
     * Constructor for InventoryServiceImpl.
     *
     * @param inventoryRepository repository used to access inventory data
     */
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Retrieves all inventory records from the database.
     *
     * @return a list of all inventory records
     */
    @Override
    public List<Inventory> getAllInventory() {
        logger.info("Entering getAllInventory()");
        List<Inventory> inventoryList = inventoryRepository.findAll();
        logger.info("Exiting getAllInventory() with {} records", inventoryList.size());
        return inventoryList;
    }

    /**
     * Retrieves an inventory record by its ID.
     *
     * @param id the ID of the inventory record
     * @return an Optional containing the inventory record if found, otherwise empty
     */
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

    /**
     * Saves a new or existing inventory record.
     *
     * @param inventory the inventory record to save
     * @return the saved inventory record with updated ID
     */
    @Override
    public Inventory saveInventory(Inventory inventory) {
        logger.info("Entering saveInventory() for productId={}, quantity={}",
                inventory.getProduct() != null ? inventory.getProduct().getId() : null,
                inventory.getQuantity());

        Inventory savedInventory = inventoryRepository.save(inventory);

        logger.info("Exiting saveInventory() with id={}", savedInventory.getId());
        return savedInventory;
    }

    /**
     * Retrieves all inventory records that fall below a specified threshold.
     *
     * @param threshold the quantity threshold used to determine low stock
     * @return a list of inventory items below the threshold
     */
    @Override
    public List<Inventory> getLowStock(int threshold) {
        logger.info("Entering getLowStock() with threshold={}", threshold);

        List<Inventory> lowStockItems = inventoryRepository.findByQuantityLessThan(threshold);

        logger.info("Exiting getLowStock() with {} low stock items", lowStockItems.size());
        return lowStockItems;
    }

    /**
     * Retrieves an inventory record by product ID.
     *
     * @param productId the ID of the associated product
     * @return an Optional containing the inventory record if found, otherwise empty
     */
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