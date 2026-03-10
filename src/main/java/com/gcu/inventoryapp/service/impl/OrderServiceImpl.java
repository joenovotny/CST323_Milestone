package com.gcu.inventoryapp.service.impl;

import com.gcu.inventoryapp.model.OrderHeader;
import com.gcu.inventoryapp.repository.OrderHeaderRepository;
import com.gcu.inventoryapp.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderHeaderRepository orderHeaderRepository;

    public OrderServiceImpl(OrderHeaderRepository orderHeaderRepository) {
        this.orderHeaderRepository = orderHeaderRepository;
    }

    @Override
    public List<OrderHeader> getAllOrders() {
        return orderHeaderRepository.findAll();
    }

    @Override
    public Optional<OrderHeader> getOrderById(Long id) {
        return orderHeaderRepository.findById(id);
    }

    @Override
    public OrderHeader saveOrder(OrderHeader orderHeader) {
        return orderHeaderRepository.save(orderHeader);
    }

    @Override
    public List<OrderHeader> getOrdersByDateRange(LocalDateTime start, LocalDateTime end) {
        return orderHeaderRepository.findByOrderDateBetween(start, end);
    }
}