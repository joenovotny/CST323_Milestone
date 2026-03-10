package com.gcu.inventoryapp.repository;

import com.gcu.inventoryapp.model.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {
    List<OrderHeader> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
}