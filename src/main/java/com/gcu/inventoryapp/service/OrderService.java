package com.gcu.inventoryapp.service;

import com.gcu.inventoryapp.model.OrderHeader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderHeader> getAllOrders();
    Optional<OrderHeader> getOrderById(Long id);
    OrderHeader saveOrder(OrderHeader orderHeader);
    List<OrderHeader> getOrdersByDateRange(LocalDateTime start, LocalDateTime end);
}