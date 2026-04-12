package com.gcu.inventoryapp.controller;

import com.gcu.inventoryapp.model.*;
import com.gcu.inventoryapp.repository.UserRepository;
import com.gcu.inventoryapp.service.CustomerService;
import com.gcu.inventoryapp.service.InventoryService;
import com.gcu.inventoryapp.service.OrderService;
import com.gcu.inventoryapp.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller responsible for handling order-related operations.
 * Provides functionality to view orders and create new orders,
 * including validation and inventory updates.
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final UserRepository userRepository;

    /**
     * Constructor for OrderController.
     *
     * @param orderService service used to manage orders
     * @param customerService service used to retrieve customer data
     * @param productService service used to retrieve product data
     * @param inventoryService service used to manage inventory
     * @param userRepository repository used to retrieve user data
     */
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

    /**
     * Displays a list of all orders.
     *
     * @param model the model used to pass order data to the view
     * @return the orders view page
     */
    @GetMapping
    public String listOrders(Model model) {
        logger.info("Entering listOrders()");
        model.addAttribute("orders", orderService.getAllOrders());
        logger.info("Exiting listOrders()");
        return "orders";
    }

    /**
     * Displays the form to create a new order.
     *
     * @param model the model used to pass form data, customers, and products to the view
     * @return the order form page
     */
    @GetMapping("/add")
    public String showOrderForm(Model model) {
        logger.info("Entering showOrderForm()");
        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("products", productService.getAllProducts());
        logger.info("Exiting showOrderForm()");
        return "order-form";
    }

    /**
     * Processes and saves a new order.
     * Performs validation on quantity and inventory availability,
     * calculates totals, and updates inventory accordingly.
     *
     * @param orderForm the submitted order form containing customer, product, and quantity
     * @param model the model used to return error messages and form data if validation fails
     * @return redirect to orders page if successful, or back to form if validation fails
     */
    @PostMapping("/save")
    public String saveOrder(@ModelAttribute OrderForm orderForm, Model model) {
        logger.info("Entering saveOrder() with customerId={}, productId={}, quantity={}",
                orderForm.getCustomerId(), orderForm.getProductId(), orderForm.getQuantity());

        Customer customer = customerService.getCustomerById(orderForm.getCustomerId()).orElseThrow();
        Product product = productService.getProductById(orderForm.getProductId()).orElseThrow();
        Inventory inventory = inventoryService.getInventoryByProductId(product.getId()).orElseThrow();

        // Validate quantity
        if (orderForm.getQuantity() <= 0) {
            logger.warn("Invalid quantity submitted in saveOrder(): {}", orderForm.getQuantity());
            model.addAttribute("error", "Quantity must be greater than 0.");
            model.addAttribute("orderForm", orderForm);
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("products", productService.getAllProducts());
            logger.info("Exiting saveOrder() with invalid quantity");
            return "order-form";
        }

        // Validate inventory availability
        if (inventory.getQuantity() < orderForm.getQuantity()) {
            logger.warn("Insufficient inventory for productId={}. Available={}, Requested={}",
                    product.getId(), inventory.getQuantity(), orderForm.getQuantity());
            model.addAttribute("error", "Not enough inventory available.");
            model.addAttribute("orderForm", orderForm);
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("products", productService.getAllProducts());
            logger.info("Exiting saveOrder() with insufficient inventory");
            return "order-form";
        }

        // Retrieve user (default first user)
        User user = userRepository.findAll().stream().findFirst().orElseThrow();

        // Create order header
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setCustomer(customer);
        orderHeader.setUser(user);
        orderHeader.setOrderDate(LocalDateTime.now());
        orderHeader.setStatus("Submitted");

        // Calculate totals
        BigDecimal unitPrice = product.getPrice();
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(orderForm.getQuantity()));

        // Create order item
        OrderItem item = new OrderItem();
        item.setOrderHeader(orderHeader);
        item.setProduct(product);
        item.setQuantity(orderForm.getQuantity());
        item.setUnitPrice(unitPrice);
        item.setLineTotal(lineTotal);

        orderHeader.setTotal(lineTotal);
        orderHeader.setItems(List.of(item));

        // Save order
        orderService.saveOrder(orderHeader);

        // Update inventory
        inventory.setQuantity(inventory.getQuantity() - orderForm.getQuantity());
        inventoryService.saveInventory(inventory);

        logger.info("Exiting saveOrder() successfully");
        return "redirect:/orders";
    }
}