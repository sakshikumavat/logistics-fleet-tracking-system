
package com.logistics.fleet.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DELIVERY_CONFIRMATIONS")
public class DeliveryConfirmation {

    @Id
    @Column(name = "CONFIRMATION_ID")
    private Long confirmationId;

    @Column(name = "SHIPMENT_ID", nullable = false, unique = true)
    private Long shipmentId;

    @Column(name = "RECEIVER_NAME", nullable = false, length = 100)
    private String receiverName;

    @Column(name = "DELIVERY_TIME", nullable = false)
    private LocalDateTime deliveryTime;

    @Column(name = "PROOF_REFERENCE", length = 500)
    private String proofReference;

    @Column(name = "NOTES", length = 1000)
    private String notes;

    public Long getConfirmationId() {
        return confirmationId;
    }

    public void setConfirmationId(Long confirmationId) {
        this.confirmationId = confirmationId;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getProofReference() {
        return proofReference;
    }

    public void setProofReference(String proofReference) {
        this.proofReference = proofReference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
