package com.gcu.inventoryapp.controller;

import com.gcu.inventoryapp.model.Customer;
import com.gcu.inventoryapp.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling all customer-related web requests.
 * Provides functionality to view, create, update, and delete customers.
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    /**
     * Constructor for CustomerController.
     *
     * @param customerService service used to manage customer operations
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Displays a list of all customers.
     *
     * @param model the model used to pass customer data to the view
     * @return the customers view page
     */
    @GetMapping
    public String listCustomers(Model model) {
        logger.info("Entering listCustomers()");
        model.addAttribute("customers", customerService.getAllCustomers());
        logger.info("Exiting listCustomers()");
        return "customers";
    }

    /**
     * Displays the form to add a new customer.
     *
     * @param model the model used to pass a new customer object to the view
     * @return the customer form page
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.info("Entering showAddForm()");
        model.addAttribute("customer", new Customer());
        logger.info("Exiting showAddForm()");
        return "customer-form";
    }

    /**
     * Saves a new or updated customer.
     *
     * @param customer the customer object submitted from the form
     * @param result contains validation results
     * @return redirect to customers page or back to form if validation fails
     */
    @PostMapping("/save")
    public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer,
                               BindingResult result) {
        logger.info("Entering saveCustomer()");

        if (result.hasErrors()) {
            logger.warn("Validation errors found in saveCustomer()");
            logger.info("Exiting saveCustomer() with validation errors");
            return "customer-form";
        }

        customerService.saveCustomer(customer);
        logger.info("Exiting saveCustomer()");
        return "redirect:/customers";
    }

    /**
     * Displays the form to edit an existing customer.
     *
     * @param id the ID of the customer to edit
     * @param model the model used to pass customer data to the view
     * @return the customer form page
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Entering showEditForm() with id={}", id);
        Customer customer = customerService.getCustomerById(id).orElseThrow();
        model.addAttribute("customer", customer);
        logger.info("Exiting showEditForm()");
        return "customer-form";
    }

    /**
     * Deletes a customer by ID.
     *
     * @param id the ID of the customer to delete
     * @return redirect to the customers page
     */
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        logger.info("Entering deleteCustomer() with id={}", id);
        customerService.deleteCustomer(id);
        logger.info("Exiting deleteCustomer()");
        return "redirect:/customers";
    }
}