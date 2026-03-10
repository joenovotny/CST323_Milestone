package com.gcu.inventoryapp.repository;

import com.gcu.inventoryapp.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByQuantityLessThan(int quantity);
}