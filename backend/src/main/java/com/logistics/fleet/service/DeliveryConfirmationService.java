
package com.logistics.fleet.service;

import com.logistics.fleet.entity.DeliveryConfirmation;
import com.logistics.fleet.entity.Shipment;
import com.logistics.fleet.repository.DeliveryConfirmationRepository;
import com.logistics.fleet.repository.ShipmentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DeliveryConfirmationService {

    private final DeliveryConfirmationRepository confirmationRepository;
    private final ShipmentRepository shipmentRepository;
    private final JdbcTemplate jdbcTemplate;

    public DeliveryConfirmationService(
            DeliveryConfirmationRepository confirmationRepository,
            ShipmentRepository shipmentRepository,
            JdbcTemplate jdbcTemplate) {
        this.confirmationRepository = confirmationRepository;
        this.shipmentRepository = shipmentRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public DeliveryConfirmation confirmDelivery(
            Long shipmentId,
            String receiverName,
            String proofReference,
            String notes) {

        if (shipmentId == null) {
            throw new IllegalArgumentException("Shipment ID is required");
        }

        if (receiverName == null || receiverName.isBlank()) {
            throw new IllegalArgumentException("Receiver name is required");
        }

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + shipmentId));

        if (!"IN_TRANSIT".equalsIgnoreCase(shipment.getStatus())) {
            throw new IllegalStateException(
                    "Only IN_TRANSIT shipments can be delivered. Current status: "
                            + shipment.getStatus());
        }

        if (confirmationRepository.existsByShipmentId(shipmentId)) {
            throw new IllegalStateException(
                    "Delivery confirmation already exists for shipment " + shipmentId);
        }

        Long confirmationId = jdbcTemplate.queryForObject(
                "SELECT DELIVERY_CONFIRMATIONS_SEQ.NEXTVAL FROM DUAL",
                Long.class);

        if (confirmationId == null) {
            throw new RuntimeException("Could not generate confirmation ID");
        }

        DeliveryConfirmation confirmation = new DeliveryConfirmation();
        confirmation.setConfirmationId(confirmationId);
        confirmation.setShipmentId(shipmentId);
        confirmation.setReceiverName(receiverName.trim());
        confirmation.setDeliveryTime(LocalDateTime.now());
        confirmation.setProofReference(
                proofReference == null || proofReference.isBlank()
                        ? null
                        : proofReference.trim());
        confirmation.setNotes(
                notes == null || notes.isBlank() ? null : notes.trim());

        DeliveryConfirmation saved = confirmationRepository.saveAndFlush(confirmation);

        String oldStatus = shipment.getStatus();
        shipment.setStatus("DELIVERED");
        shipmentRepository.saveAndFlush(shipment);

        Long historyId = jdbcTemplate.queryForObject(
                "SELECT NVL(MAX(HISTORY_ID), 0) + 1 " +
                        "FROM SHIPMENT_STATUS_HISTORY",
                Long.class);

        jdbcTemplate.update(
                "INSERT INTO SHIPMENT_STATUS_HISTORY " +
                        "(HISTORY_ID, SHIPMENT_ID, OLD_STATUS, NEW_STATUS, " +
                        "CHANGED_AT, REMARKS) " +
                        "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?)",
                historyId,
                shipmentId,
                oldStatus,
                "DELIVERED",
                "Delivery confirmed. Receiver: " + receiverName.trim());

        return saved;
    }

    @Transactional(readOnly = true)
    public DeliveryConfirmation getConfirmation(Long shipmentId) {
        return confirmationRepository.findByShipmentId(shipmentId)
                .orElseThrow(() -> new RuntimeException(
                        "Delivery confirmation not found for shipment "
                                + shipmentId));
    }
}
