package com.gcu.inventoryapp.repository;

import com.gcu.inventoryapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}