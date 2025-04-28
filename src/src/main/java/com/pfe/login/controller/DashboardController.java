package com.pfe.login.controller;

import com.pfe.login.model.Article;
import com.pfe.login.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getDashboardMetrics() {
        return ResponseEntity.ok(dashboardService.getDashboardMetrics());
    }

    @GetMapping("/category-distribution")
    public ResponseEntity<Map<String, Integer>> getCategoryDistribution() {
        return ResponseEntity.ok(dashboardService.getCategoryDistribution());
    }

    @GetMapping("/manufacturer-distribution")
    public ResponseEntity<Map<String, Integer>> getManufacturerDistribution() {
        return ResponseEntity.ok(dashboardService.getManufacturerDistribution());
    }

    @GetMapping("/stock-evolution")
    public ResponseEntity<List<Map<String, Object>>> getStockEvolution() {
        return ResponseEntity.ok(dashboardService.getStockEvolution());
    }


    @GetMapping("/stock-items")
    public ResponseEntity<List<Article>> getStockItems() {
        return ResponseEntity.ok(dashboardService.getStockItems());
    }
}