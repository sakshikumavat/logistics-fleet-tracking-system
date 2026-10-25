package com.logistics.fleet.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ASSIGNMENTS")
public class Assignment {

    @Id
    @Column(name = "ASSIGNMENT_ID")
    private Long assignmentId;

    @Column(name = "SHIPMENT_ID", nullable = false)
    private Long shipmentId;

    @Column(name = "DRIVER_ID", nullable = false)
    private Long driverId;

    @Column(name = "VEHICLE_ID", nullable = false)
    private Long vehicleId;

    @Column(name = "ASSIGNED_AT")
    private LocalDateTime assignedAt;

    @Column(name = "STATUS", length = 30)
    private String status;

    public Assignment() {}

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}