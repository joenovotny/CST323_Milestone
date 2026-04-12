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

/**
 * Service implementation responsible for handling product-related business logic.
 * Provides operations for retrieving, saving, and safely deleting products.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * Constructor for ProductServiceImpl.
     *
     * @param productRepository repository used to access product data
     * @param inventoryRepository repository used to check inventory relationships
     * @param orderItemRepository repository used to check order item relationships
     */
    public ProductServiceImpl(ProductRepository productRepository,
                              InventoryRepository inventoryRepository,
                              OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products
     */
    @Override
    public List<Product> getAllProducts() {
        logger.info("Entering getAllProducts()");
        List<Product> products = productRepository.findAll();
        logger.info("Exiting getAllProducts() with {} products", products.size());
        return products;
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product
     * @return an Optional containing the product if found, otherwise empty
     */
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

    /**
     * Saves a new or existing product to the database.
     *
     * @param product the product to save
     * @return the saved product with updated ID
     */
    @Override
    public Product saveProduct(Product product) {
        logger.info("Entering saveProduct() for product name={}", product.getName());
        Product savedProduct = productRepository.save(product);
        logger.info("Exiting saveProduct() with id={}", savedProduct.getId());
        return savedProduct;
    }

    /**
     * Deletes a product by ID if it is not associated with inventory or order items.
     *
     * @param id the ID of the product to delete
     * @return true if the product was successfully deleted, false if it is linked to other records
     */
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