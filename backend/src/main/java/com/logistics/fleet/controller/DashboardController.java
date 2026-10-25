
package com.logistics.fleet.controller;

import com.logistics.fleet.dto.DashboardSummary;
import com.logistics.fleet.service.DashboardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public DashboardSummary getSummary() {
        return dashboardService.getSummary();
    }

    @GetMapping("/shipments-by-status")
    public List<Map<String, Object>> getShipmentStatuses() {
        return dashboardService.getShipmentStatuses();
    }
}
