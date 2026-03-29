package com.gcu.inventoryapp.repository;

import com.gcu.inventoryapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi WHERE oi.orderHeader.orderDate BETWEEN :start AND :end")
    List<OrderItem> findSalesReportByDateRange(LocalDateTime start, LocalDateTime end);
}