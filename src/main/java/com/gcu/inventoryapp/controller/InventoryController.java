package com.gcu.inventoryapp.controller;

import com.gcu.inventoryapp.model.Inventory;
import com.gcu.inventoryapp.model.Product;
import com.gcu.inventoryapp.service.InventoryService;
import com.gcu.inventoryapp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;

    public InventoryController(InventoryService inventoryService, ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    @GetMapping
    public String listInventory(Model model) {
        model.addAttribute("inventoryList", inventoryService.getAllInventory());
        return "inventory";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("inventory", new Inventory());
        model.addAttribute("products", productService.getAllProducts());
        return "inventory-form";
    }

    @PostMapping("/save")
    public String saveInventory(@Valid @ModelAttribute("inventory") Inventory inventory,
                                BindingResult result,
                                @RequestParam("productId") Long productId,
                                Model model) {

        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            return "inventory-form";
        }

        Product product = productService.getProductById(productId).orElseThrow();
        inventory.setProduct(product);

        inventoryService.saveInventory(inventory);
        return "redirect:/inventory";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Inventory inventory = inventoryService.getInventoryById(id).orElseThrow();
        model.addAttribute("inventory", inventory);
        model.addAttribute("products", productService.getAllProducts());
        return "inventory-form";
    }
}