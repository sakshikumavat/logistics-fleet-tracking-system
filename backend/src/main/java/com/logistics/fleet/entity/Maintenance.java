
package com.logistics.fleet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "MAINTENANCE")
public class Maintenance {

    @Id
    @Column(name = "MAINTENANCE_ID")
    private Long maintenanceId;

    @Column(name = "VEHICLE_ID", nullable = false)
    private Long vehicleId;

    @Column(name = "MAINTENANCE_DATE", nullable = false)
    private LocalDate maintenanceDate;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "COST", nullable = false)
    private BigDecimal cost;

    @Column(name = "MAINTENANCE_STATUS", nullable = false)
    private String maintenanceStatus;

    public Maintenance() {
    }

    public Long getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(Long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(String maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }
}
