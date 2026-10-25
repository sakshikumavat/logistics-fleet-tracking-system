package com.logistics.fleet.controller;

import com.logistics.fleet.dto.DashboardSummary;
import com.logistics.fleet.entity.FuelRecord;
import com.logistics.fleet.entity.Maintenance;
import com.logistics.fleet.repository.FuelRecordRepository;
import com.logistics.fleet.repository.MaintenanceRepository;
import com.logistics.fleet.repository.VehicleRepository;
import com.logistics.fleet.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final DashboardService dashboardService;
    private final FuelRecordRepository fuelRecordRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;

    public ReportController(DashboardService dashboardService,
                            FuelRecordRepository fuelRecordRepository,
                            MaintenanceRepository maintenanceRepository,
                            VehicleRepository vehicleRepository) {
        this.dashboardService = dashboardService;
        this.fuelRecordRepository = fuelRecordRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping("/dashboard")
    public DashboardSummary getDashboardReport() {
        return dashboardService.getSummary();
    }

    @GetMapping("/fuel")
    public List<FuelRecord> getFuelReport() {
        return fuelRecordRepository.findAll();
    }

    @GetMapping("/maintenance")
    public List<Maintenance> getMaintenanceReport() {
        return maintenanceRepository.findAll();
    }

    @GetMapping("/utilization")
    public Map<String, Object> getUtilizationReport() {
        long totalVehicles = vehicleRepository.count();
        long availableVehicles = vehicleRepository.findByVehicleStatus("AVAILABLE").size();
        long onRouteVehicles = totalVehicles - availableVehicles;
        double utilizationRate = totalVehicles > 0 ? ((double) onRouteVehicles / totalVehicles) * 100.0 : 0.0;

        Map<String, Object> report = new HashMap<>();
        report.put("totalVehicles", totalVehicles);
        report.put("availableVehicles", availableVehicles);
        report.put("activeVehicles", onRouteVehicles);
        report.put("utilizationRatePercent", Math.round(utilizationRate * 10.0) / 10.0);
        return report;
    }
}
