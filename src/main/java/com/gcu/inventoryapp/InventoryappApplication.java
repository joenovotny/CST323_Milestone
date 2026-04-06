package com.gcu.inventoryapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryappApplication {

	private static final Logger logger = LoggerFactory.getLogger(InventoryappApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Inventory Application...");
		SpringApplication.run(InventoryappApplication.class, args);
		logger.info("Inventory Application started successfully.");
	}

}