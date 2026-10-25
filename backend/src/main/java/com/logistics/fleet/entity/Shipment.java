package com.logistics.fleet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "SHIPMENTS")
public class Shipment {

    @Id
    @Column(name = "SHIPMENT_ID")
    private Long shipmentId;

    @Column(name = "TRACKING_NUMBER", nullable = false, length = 50)
    private String trackingNumber;

    @Column(name = "CUSTOMER_NAME", length = 100)
    private String customerName;

    @Column(name = "CUSTOMER_PHONE", length = 20)
    private String customerPhone;

    @Column(name = "PICKUP_ADDRESS", nullable = false, length = 300)
    private String pickupAddress;

    @Column(name = "DELIVERY_ADDRESS", nullable = false, length = 300)
    private String deliveryAddress;

    @Column(name = "WEIGHT_KG", precision = 12, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "STATUS", length = 30)
    private String status;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "EXPECTED_DELIVERY")
    private LocalDate expectedDelivery;

    public Shipment() {}

    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDate getExpectedDelivery() { return expectedDelivery; }
    public void setExpectedDelivery(LocalDate expectedDelivery) { this.expectedDelivery = expectedDelivery; }
}