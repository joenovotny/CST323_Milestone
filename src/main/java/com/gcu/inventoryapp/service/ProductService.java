package com.gcu.inventoryapp.service;

import com.gcu.inventoryapp.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product saveProduct(Product product);
    boolean deleteProduct(Long id);
}