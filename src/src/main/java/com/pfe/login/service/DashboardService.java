package com.pfe.login.service;

import com.pfe.login.model.Article;
import com.pfe.login.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public Map<String, Object> getDashboardMetrics() {
        LocalDate today = LocalDate.now();
        LocalDate threeMonthsLater = today.plusMonths(3);

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalArticles", dashboardRepository.countTotalArticles());
        metrics.put("totalQuantity", dashboardRepository.calculateTotalQuantity());
        metrics.put("lowStockItemsCount", dashboardRepository.countLowStockItems());
        metrics.put("expiringItemsCount", dashboardRepository.countExpiringItems(today, threeMonthsLater));
        metrics.put("totalEntries", dashboardRepository.calculateTotalEntries());
        metrics.put("totalExits", dashboardRepository.calculateTotalExits());

        return metrics;
    }

    public Map<String, Integer> getCategoryDistribution() {
        return dashboardRepository.getCategoryDistribution().stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> ((Number) arr[1]).intValue()
                ));
    }

    public Map<String, Integer> getManufacturerDistribution() {
        return dashboardRepository.getManufacturerDistribution().stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> ((Number) arr[1]).intValue()
                ));
    }

    public List<Map<String, Object>> getStockEvolution() {
        return dashboardRepository.getStockEvolution().stream()
                .map(arr -> Map.of(
                        "month", arr[0],
                        "totalQuantity", arr[1]
                ))
                .collect(Collectors.toList());
    }


    public List<Article> getStockItems() {
        return dashboardRepository.findStockItems();
    }
}