package com.gcu.inventoryapp.controller;

import com.gcu.inventoryapp.model.*;
import com.gcu.inventoryapp.repository.UserRepository;
import com.gcu.inventoryapp.service.CustomerService;
import com.gcu.inventoryapp.service.InventoryService;
import com.gcu.inventoryapp.service.OrderService;
import com.gcu.inventoryapp.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService,
                           CustomerService customerService,
                           ProductService productService,
                           InventoryService inventoryService,
                           UserRepository userRepository) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
        this.inventoryService = inventoryService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders";
    }

    @GetMapping("/add")
    public String showOrderForm(Model model) {
        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("products", productService.getAllProducts());
        return "order-form";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute OrderForm orderForm, Model model) {
        Customer customer = customerService.getCustomerById(orderForm.getCustomerId()).orElseThrow();
        Product product = productService.getProductById(orderForm.getProductId()).orElseThrow();
        Inventory inventory = inventoryService.getInventoryByProductId(product.getId()).orElseThrow();

        if (orderForm.getQuantity() <= 0) {
            model.addAttribute("error", "Quantity must be greater than 0.");
            model.addAttribute("orderForm", orderForm);
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("products", productService.getAllProducts());
            return "order-form";
        }

        if (inventory.getQuantity() < orderForm.getQuantity()) {
            model.addAttribute("error", "Not enough inventory available.");
            model.addAttribute("orderForm", orderForm);
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("products", productService.getAllProducts());
            return "order-form";
        }

        User user = userRepository.findAll().stream().findFirst().orElseThrow();

        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setCustomer(customer);
        orderHeader.setUser(user);
        orderHeader.setOrderDate(LocalDateTime.now());
        orderHeader.setStatus("Submitted");

        BigDecimal unitPrice = product.getPrice();
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(orderForm.getQuantity()));

        OrderItem item = new OrderItem();
        item.setOrderHeader(orderHeader);
        item.setProduct(product);
        item.setQuantity(orderForm.getQuantity());
        item.setUnitPrice(unitPrice);
        item.setLineTotal(lineTotal);

        orderHeader.setTotal(lineTotal);
        orderHeader.setItems(List.of(item));

        orderService.saveOrder(orderHeader);

        inventory.setQuantity(inventory.getQuantity() - orderForm.getQuantity());
        inventoryService.saveInventory(inventory);

        return "redirect:/orders";
    }
}