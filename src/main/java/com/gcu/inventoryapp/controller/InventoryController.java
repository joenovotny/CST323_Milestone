package com.gcu.inventoryapp.controller;

import com.gcu.inventoryapp.model.Inventory;
import com.gcu.inventoryapp.model.Product;
import com.gcu.inventoryapp.service.InventoryService;
import com.gcu.inventoryapp.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling inventory-related operations.
 * Provides functionality to view, create, and update inventory records.
 */
@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;
    private final ProductService productService;

    /**
     * Constructor for InventoryController.
     *
     * @param inventoryService service used to manage inventory operations
     * @param productService service used to retrieve product data
     */
    public InventoryController(InventoryService inventoryService, ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    /**
     * Displays a list of all inventory records.
     *
     * @param model the model used to pass inventory data to the view
     * @return the inventory view page
     */
    @GetMapping
    public String listInventory(Model model) {
        logger.info("Entering listInventory()");
        model.addAttribute("inventoryList", inventoryService.getAllInventory());
        logger.info("Exiting listInventory()");
        return "inventory";
    }

    /**
     * Displays the form to add a new inventory record.
     *
     * @param model the model used to pass inventory and product data to the view
     * @return the inventory form page
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.info("Entering showAddForm()");
        model.addAttribute("inventory", new Inventory());
        model.addAttribute("products", productService.getAllProducts());
        logger.info("Exiting showAddForm()");
        return "inventory-form";
    }

    /**
     * Saves a new or updated inventory record.
     * Validates input and prevents duplicate inventory entries for the same product.
     *
     * @param inventory the inventory object submitted from the form
     * @param result contains validation results
     * @param productId the ID of the associated product
     * @param model the model used to return data to the view in case of errors
     * @return redirect to inventory page or back to form if validation fails
     */
    @PostMapping("/save")
    public String saveInventory(@Valid @ModelAttribute("inventory") Inventory inventory,
                                BindingResult result,
                                @RequestParam("productId") Long productId,
                                Model model) {

        logger.info("Entering saveInventory() with productId={}", productId);

        if (result.hasErrors()) {
            logger.warn("Validation errors in saveInventory()");
            model.addAttribute("products", productService.getAllProducts());
            logger.info("Exiting saveInventory() with validation errors");
            return "inventory-form";
        }

        if (inventory.getId() == null &&
            inventoryService.getInventoryByProductId(productId).isPresent()) {

            logger.warn("Duplicate inventory detected for productId={}", productId);

            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("duplicateError",
                "Inventory already exists for that product. Please edit the existing record.");

            logger.info("Exiting saveInventory() due to duplicate");
            return "inventory-form";
        }

        Product product = productService.getProductById(productId).orElseThrow();
        inventory.setProduct(product);

        inventoryService.saveInventory(inventory);

        logger.info("Exiting saveInventory()");
        return "redirect:/inventory";
    }

    /**
     * Displays the form to edit an existing inventory record.
     *
     * @param id the ID of the inventory record to edit
     * @param model the model used to pass inventory and product data to the view
     * @return the inventory form page
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Entering showEditForm() with id={}", id);

        Inventory inventory = inventoryService.getInventoryById(id).orElseThrow();
        model.addAttribute("inventory", inventory);
        model.addAttribute("products", productService.getAllProducts());

        logger.info("Exiting showEditForm()");
        return "inventory-form";
    }
}