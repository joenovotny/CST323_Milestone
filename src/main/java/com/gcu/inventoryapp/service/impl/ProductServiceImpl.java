package com.gcu.inventoryapp.service.impl;

import com.gcu.inventoryapp.model.Product;
import com.gcu.inventoryapp.repository.InventoryRepository;
import com.gcu.inventoryapp.repository.OrderItemRepository;
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
    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              InventoryRepository inventoryRepository,
                              OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderItemRepository = orderItemRepository;
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
    public boolean deleteProduct(Long id) {
        logger.info("Entering deleteProduct() with id={}", id);

boolean hasInventory = inventoryRepository.existsByProduct_Id(id);
boolean hasOrderItems = orderItemRepository.existsByProduct_Id(id);

        if (hasInventory || hasOrderItems) {
            logger.warn("Cannot delete product id={} because it is linked to inventory or order items", id);
            logger.info("Exiting deleteProduct() with failure");
            return false;
        }

        productRepository.deleteById(id);
        logger.info("Exiting deleteProduct() successfully");
        return true;
    }
}