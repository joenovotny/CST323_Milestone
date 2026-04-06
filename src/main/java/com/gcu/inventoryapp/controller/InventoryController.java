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

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;
    private final ProductService productService;

    public InventoryController(InventoryService inventoryService, ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    @GetMapping
    public String listInventory(Model model) {
        logger.info("Entering listInventory()");
        model.addAttribute("inventoryList", inventoryService.getAllInventory());
        logger.info("Exiting listInventory()");
        return "inventory";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.info("Entering showAddForm()");
        model.addAttribute("inventory", new Inventory());
        model.addAttribute("products", productService.getAllProducts());
        logger.info("Exiting showAddForm()");
        return "inventory-form";
    }

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