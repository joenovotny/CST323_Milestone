package com.gcu.inventoryapp.service.impl;

import com.gcu.inventoryapp.model.Customer;
import com.gcu.inventoryapp.repository.CustomerRepository;
import com.gcu.inventoryapp.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation responsible for handling customer-related business logic.
 * Interacts with the CustomerRepository to perform CRUD operations.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    /**
     * Constructor for CustomerServiceImpl.
     *
     * @param customerRepository repository used to access customer data
     */
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return a list of all customers
     */
    @Override
    public List<Customer> getAllCustomers() {
        logger.info("Entering getAllCustomers()");
        List<Customer> customers = customerRepository.findAll();
        logger.info("Exiting getAllCustomers() with {} customers", customers.size());
        return customers;
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id the ID of the customer
     * @return an Optional containing the customer if found, otherwise empty
     */
    @Override
    public Optional<Customer> getCustomerById(Long id) {
        logger.info("Entering getCustomerById() with id={}", id);

        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            logger.info("Customer found for id={}", id);
        } else {
            logger.warn("Customer NOT found for id={}", id);
        }

        logger.info("Exiting getCustomerById()");
        return customer;
    }

    /**
     * Saves a new or existing customer to the database.
     *
     * @param customer the customer to save
     * @return the saved customer with updated ID
     */
    @Override
    public Customer saveCustomer(Customer customer) {
        logger.info("Entering saveCustomer() for customer email={}", customer.getEmail());

        Customer savedCustomer = customerRepository.save(customer);

        logger.info("Exiting saveCustomer() with id={}", savedCustomer.getId());
        return savedCustomer;
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param id the ID of the customer to delete
     */
    @Override
    public void deleteCustomer(Long id) {
        logger.info("Entering deleteCustomer() with id={}", id);
        customerRepository.deleteById(id);
        logger.info("Exiting deleteCustomer()");
    }
}