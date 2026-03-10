package com.gcu.inventoryapp.repository;

import com.gcu.inventoryapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}