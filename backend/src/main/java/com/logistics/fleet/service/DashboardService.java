
package com.logistics.fleet.service;

import com.logistics.fleet.dto.DashboardSummary;
import com.logistics.fleet.repository.DashboardRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final DashboardRepository repository;

    public DashboardService(DashboardRepository repository) {
        this.repository = repository;
    }

    public DashboardSummary getSummary() {
        DashboardSummary summary = new DashboardSummary();

        summary.setTotalVehicles(repository.count(
                "SELECT COUNT(*) FROM VEHICLES"));

        summary.setAvailableVehicles(repository.count(
                "SELECT COUNT(*) FROM VEHICLES " +
                "WHERE VEHICLE_STATUS = 'AVAILABLE'"));

        summary.setTotalShipments(repository.count(
                "SELECT COUNT(*) FROM SHIPMENTS"));

        summary.setActiveTrips(repository.count(
                "SELECT COUNT(*) FROM SHIPMENTS " +
                "WHERE UPPER(STATUS) IN ('IN_TRANSIT', 'ASSIGNED')"));

        summary.setDeliveredShipments(repository.count(
                "SELECT COUNT(*) FROM SHIPMENTS " +
                "WHERE UPPER(STATUS) = 'DELIVERED'"));

        summary.setTotalFuelExpense(repository.amount(
                "SELECT NVL(SUM(TOTAL_COST), 0) FROM FUEL_RECORDS"));

        summary.setTotalMaintenanceExpense(repository.amount(
                "SELECT NVL(SUM(COST), 0) FROM MAINTENANCE"));

        return summary;
    }

    public List<Map<String, Object>> getShipmentStatuses() {
        return repository.getShipmentStatuses();
    }
}
