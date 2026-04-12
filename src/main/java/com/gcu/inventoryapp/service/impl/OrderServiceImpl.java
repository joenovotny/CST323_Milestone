package com.gcu.inventoryapp.service.impl;

import com.gcu.inventoryapp.model.OrderHeader;
import com.gcu.inventoryapp.repository.OrderHeaderRepository;
import com.gcu.inventoryapp.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation responsible for handling order-related business logic.
 * Provides operations for retrieving, saving, and filtering orders.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderHeaderRepository orderHeaderRepository;

    /**
     * Constructor for OrderServiceImpl.
     *
     * @param orderHeaderRepository repository used to access order data
     */
    public OrderServiceImpl(OrderHeaderRepository orderHeaderRepository) {
        this.orderHeaderRepository = orderHeaderRepository;
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return a list of all orders
     */
    @Override
    public List<OrderHeader> getAllOrders() {
        logger.info("Entering getAllOrders()");
        List<OrderHeader> orders = orderHeaderRepository.findAll();
        logger.info("Exiting getAllOrders() with {} orders", orders.size());
        return orders;
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order
     * @return an Optional containing the order if found, otherwise empty
     */
    @Override
    public Optional<OrderHeader> getOrderById(Long id) {
        logger.info("Entering getOrderById() with id={}", id);

        Optional<OrderHeader> order = orderHeaderRepository.findById(id);

        if (order.isPresent()) {
            logger.info("Order found for id={}", id);
        } else {
            logger.warn("Order NOT found for id={}", id);
        }

        logger.info("Exiting getOrderById()");
        return order;
    }

    /**
     * Saves a new or existing order to the database.
     *
     * @param orderHeader the order to save
     * @return the saved order with updated ID
     */
    @Override
    public OrderHeader saveOrder(OrderHeader orderHeader) {
        logger.info("Entering saveOrder() for customerId={}, total={}",
                orderHeader.getCustomer().getId(),
                orderHeader.getTotal());

        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);

        logger.info("Exiting saveOrder() with orderId={}", savedOrder.getId());
        return savedOrder;
    }

    /**
     * Retrieves orders within a specified date range.
     *
     * @param start the start date and time
     * @param end the end date and time
     * @return a list of orders within the specified date range
     */
    @Override
    public List<OrderHeader> getOrdersByDateRange(LocalDateTime start, LocalDateTime end) {
        logger.info("Entering getOrdersByDateRange() from {} to {}", start, end);

        List<OrderHeader> orders = orderHeaderRepository.findByOrderDateBetween(start, end);

        logger.info("Exiting getOrdersByDateRange() with {} orders", orders.size());
        return orders;
    }
}