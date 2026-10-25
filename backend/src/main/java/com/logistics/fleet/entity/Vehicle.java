
package com.logistics.fleet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "VEHICLES")
public class Vehicle {

    @Id
    @Column(name = "VEHICLE_ID")
    private Long vehicleId;

    @Column(name = "REGISTRATION_NO", nullable = false)
    private String registrationNo;

    @Column(name = "VEHICLE_NAME", nullable = false)
    private String vehicleName;

    @Column(name = "VEHICLE_TYPE", nullable = false)
    private String vehicleType;

    @Column(name = "DAILY_RATE", nullable = false)
    private BigDecimal dailyRate;

    @Column(name = "VEHICLE_STATUS", nullable = false)
    private String vehicleStatus;

    public Vehicle() {
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }
}
