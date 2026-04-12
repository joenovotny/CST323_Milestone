package com.gcu.inventoryapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for handling navigation to core application pages
 * such as login and dashboard.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Handles the root URL and directs users to the login page.
     *
     * @return the login view page
     */
    @GetMapping("/")
    public String home() {
        logger.info("Entering home()");
        logger.info("Exiting home()");
        return "login";
    }

    /**
     * Displays the login page.
     *
     * @return the login view page
     */
    @GetMapping("/login")
    public String login() {
        logger.info("Entering login()");
        logger.info("Exiting login()");
        return "login";
    }

    /**
     * Displays the main dashboard page after login.
     *
     * @return the dashboard view page
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        logger.info("Entering dashboard()");
        logger.info("Exiting dashboard()");
        return "dashboard";
    }
}