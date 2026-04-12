package com.gcu.inventoryapp.controller;

import com.gcu.inventoryapp.model.Product;
import com.gcu.inventoryapp.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling product-related operations.
 * Provides functionality to view, create, update, and delete products.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    /**
     * Constructor for ProductController.
     *
     * @param productService service used to manage product operations
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Displays a list of all products.
     * Also handles optional error messaging for failed delete operations.
     *
     * @param model the model used to pass product data to the view
     * @param deleteError optional error message if delete fails
     * @return the products view page
     */
    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(value = "deleteError", required = false) String deleteError) {
        logger.info("Entering listProducts()");
        model.addAttribute("products", productService.getAllProducts());

        if (deleteError != null) {
            model.addAttribute("deleteError", deleteError);
        }

        logger.info("Exiting listProducts()");
        return "products";
    }

    /**
     * Displays the form to add a new product.
     *
     * @param model the model used to pass a new product object to the view
     * @return the product form page
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.info("Entering showAddForm()");
        model.addAttribute("product", new Product());
        logger.info("Exiting showAddForm()");
        return "product-form";
    }

    /**
     * Saves a new or updated product.
     *
     * @param product the product object submitted from the form
     * @param result contains validation results
     * @return redirect to products page or back to form if validation fails
     */
    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result) {

        logger.info("Entering saveProduct()");

        if (result.hasErrors()) {
            logger.warn("Validation errors in saveProduct()");
            logger.info("Exiting saveProduct() with validation errors");
            return "product-form";
        }

        productService.saveProduct(product);
        logger.info("Exiting saveProduct()");
        return "redirect:/products";
    }

    /**
     * Displays the form to edit an existing product.
     *
     * @param id the ID of the product to edit
     * @param model the model used to pass product data to the view
     * @return the product form page
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Entering showEditForm() with id={}", id);

        Product product = productService.getProductById(id).orElseThrow();
        model.addAttribute("product", product);

        logger.info("Exiting showEditForm()");
        return "product-form";
    }

    /**
     * Deletes a product by ID if it is not associated with inventory or orders.
     *
     * @param id the ID of the product to delete
     * @return redirect to products page with optional error message if delete fails
     */
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        logger.info("Entering deleteProduct() with id={}", id);

        boolean deleted = productService.deleteProduct(id);

        if (!deleted) {
            logger.warn("Delete blocked for product id={}", id);
            return "redirect:/products?deleteError=Product cannot be deleted because it is linked to inventory or orders.";
        }

        logger.info("Exiting deleteProduct()");
        return "redirect:/products";
    }
}