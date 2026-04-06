package com.gcu.inventoryapp.repository;

import com.gcu.inventoryapp.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByQuantityLessThan(int quantity);
    Optional<Inventory> findByProductId(Long productId);
    boolean existsByProduct_Id(Long productId);
}