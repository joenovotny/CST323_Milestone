package com.gcu.inventoryapp.controller;

import com.gcu.inventoryapp.repository.OrderItemRepository;
import com.gcu.inventoryapp.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Controller responsible for generating application reports.
 * Provides functionality for low stock reporting and sales reporting.
 */
@Controller
@RequestMapping("/reports")
public class ReportsController {

    private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);

    private final InventoryService inventoryService;
    private final OrderItemRepository orderItemRepository;

    /**
     * Constructor for ReportsController.
     *
     * @param inventoryService service used to retrieve inventory data
     * @param orderItemRepository repository used to retrieve order item data for reporting
     */
    public ReportsController(InventoryService inventoryService,
                             OrderItemRepository orderItemRepository) {
        this.inventoryService = inventoryService;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Generates a low stock report based on a predefined threshold.
     *
     * @param model the model used to pass inventory data to the view
     * @return the low stock report view page
     */
    @GetMapping("/low-stock")
    public String lowStockReport(Model model) {
        logger.info("Entering lowStockReport()");
        model.addAttribute("inventoryList", inventoryService.getLowStock(5));
        logger.info("Exiting lowStockReport()");
        return "low-stock-report";
    }

    /**
     * Generates a sales report within a specified date range.
     * If no dates are provided, the report page loads without data.
     *
     * @param startDate the start date for the report (optional)
     * @param endDate the end date for the report (optional)
     * @param model the model used to pass report data to the view
     * @return the sales report view page
     */
    @GetMapping("/sales")
    public String salesReport(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        logger.info("Entering salesReport() with startDate={} and endDate={}", startDate, endDate);

        if (startDate != null && endDate != null) {
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(23, 59, 59);
            model.addAttribute("salesItems", orderItemRepository.findSalesReportByDateRange(start, end));
            logger.info("Sales report generated for date range {} to {}", startDate, endDate);
        } else {
            logger.info("Sales report opened without date range");
        }

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        logger.info("Exiting salesReport()");
        return "sales-report";
    }
}