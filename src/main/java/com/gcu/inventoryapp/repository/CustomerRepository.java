package com.gcu.inventoryapp.repository;

import com.gcu.inventoryapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}