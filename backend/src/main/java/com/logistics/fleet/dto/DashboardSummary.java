
package com.logistics.fleet.dto;

import java.math.BigDecimal;

public class DashboardSummary {

    private long totalVehicles;
    private long availableVehicles;
    private long totalShipments;
    private long activeTrips;
    private long deliveredShipments;
    private BigDecimal totalFuelExpense;
    private BigDecimal totalMaintenanceExpense;

    public DashboardSummary() {
    }

    public long getTotalVehicles() {
        return totalVehicles;
    }

    public void setTotalVehicles(long totalVehicles) {
        this.totalVehicles = totalVehicles;
    }

    public long getAvailableVehicles() {
        return availableVehicles;
    }

    public void setAvailableVehicles(long availableVehicles) {
        this.availableVehicles = availableVehicles;
    }

    public long getTotalShipments() {
        return totalShipments;
    }

    public void setTotalShipments(long totalShipments) {
        this.totalShipments = totalShipments;
    }

    public long getActiveTrips() {
        return activeTrips;
    }

    public void setActiveTrips(long activeTrips) {
        this.activeTrips = activeTrips;
    }

    public long getDeliveredShipments() {
        return deliveredShipments;
    }

    public void setDeliveredShipments(long deliveredShipments) {
        this.deliveredShipments = deliveredShipments;
    }

    public BigDecimal getTotalFuelExpense() {
        return totalFuelExpense;
    }

    public void setTotalFuelExpense(BigDecimal totalFuelExpense) {
        this.totalFuelExpense = totalFuelExpense;
    }

    public BigDecimal getTotalMaintenanceExpense() {
        return totalMaintenanceExpense;
    }

    public void setTotalMaintenanceExpense(
            BigDecimal totalMaintenanceExpense) {
        this.totalMaintenanceExpense = totalMaintenanceExpense;
    }
}
