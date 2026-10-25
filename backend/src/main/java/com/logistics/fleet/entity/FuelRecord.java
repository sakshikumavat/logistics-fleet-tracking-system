
package com.logistics.fleet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "FUEL_RECORDS")
public class FuelRecord {

    @Id
    @Column(name = "FUEL_ID")
    private Long fuelId;

    @Column(name = "VEHICLE_ID", nullable = false)
    private Long vehicleId;

    @Column(name = "FUEL_DATE", nullable = false)
    private LocalDate fuelDate;

    @Column(name = "FUEL_TYPE", nullable = false)
    private String fuelType;

    @Column(name = "LITRES", nullable = false)
    private BigDecimal litres;

    @Column(name = "PRICE_PER_LITRE", nullable = false)
    private BigDecimal pricePerLitre;

    @Column(name = "TOTAL_COST", nullable = false)
    private BigDecimal totalCost;

    @Column(name = "ODOMETER_READING")
    private BigDecimal odometerReading;

    @Column(name = "FUEL_STATION")
    private String fuelStation;

    public FuelRecord() {
    }

    public Long getFuelId() {
        return fuelId;
    }

    public void setFuelId(Long fuelId) {
        this.fuelId = fuelId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDate getFuelDate() {
        return fuelDate;
    }

    public void setFuelDate(LocalDate fuelDate) {
        this.fuelDate = fuelDate;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public BigDecimal getLitres() {
        return litres;
    }

    public void setLitres(BigDecimal litres) {
        this.litres = litres;
    }

    public BigDecimal getPricePerLitre() {
        return pricePerLitre;
    }

    public void setPricePerLitre(BigDecimal pricePerLitre) {
        this.pricePerLitre = pricePerLitre;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getOdometerReading() {
        return odometerReading;
    }

    public void setOdometerReading(BigDecimal odometerReading) {
        this.odometerReading = odometerReading;
    }

    public String getFuelStation() {
        return fuelStation;
    }

    public void setFuelStation(String fuelStation) {
        this.fuelStation = fuelStation;
    }
}
