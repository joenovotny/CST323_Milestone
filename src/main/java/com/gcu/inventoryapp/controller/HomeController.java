package com.gcu.inventoryapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String home() {
        logger.info("Entering home()");
        logger.info("Exiting home()");
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        logger.info("Entering login()");
        logger.info("Exiting login()");
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        logger.info("Entering dashboard()");
        logger.info("Exiting dashboard()");
        return "dashboard";
    }
}