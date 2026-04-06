package com.gcu.inventoryapp.service.impl;

import com.gcu.inventoryapp.model.Product;
import com.gcu.inventoryapp.repository.ProductRepository;
import com.gcu.inventoryapp.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        logger.info("Entering getAllProducts()");
        List<Product> products = productRepository.findAll();
        logger.info("Exiting getAllProducts() with {} products", products.size());
        return products;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        logger.info("Entering getProductById() with id={}", id);
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            logger.info("Product found for id={}", id);
        } else {
            logger.warn("Product NOT found for id={}", id);
        }

        logger.info("Exiting getProductById()");
        return product;
    }

    @Override
    public Product saveProduct(Product product) {
        logger.info("Entering saveProduct() for product name={}", product.getName());
        Product savedProduct = productRepository.save(product);
        logger.info("Exiting saveProduct() with id={}", savedProduct.getId());
        return savedProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        logger.info("Entering deleteProduct() with id={}", id);
        productRepository.deleteById(id);
        logger.info("Exiting deleteProduct()");
    }
}