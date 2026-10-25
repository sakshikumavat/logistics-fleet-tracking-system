package com.logistics.fleet.service;

import com.logistics.fleet.entity.Shipment;
import com.logistics.fleet.repository.ShipmentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;
    private final JdbcTemplate jdbcTemplate;

    public ShipmentService(ShipmentRepository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Shipment> getAllShipments() {
        return repository.findAll();
    }

    public Shipment getShipmentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Shipment not found: " + id));
    }

    public Shipment createShipment(Shipment shipment) {

        if (shipment.getShipmentId() == null || shipment.getShipmentId() == 0L) {
            Long maxId = repository.findMaxShipmentId();
            shipment.setShipmentId((maxId != null ? maxId : 0L) + 1L);
        }

        if (shipment.getTrackingNumber() == null
                || shipment.getTrackingNumber().isBlank()) {
            shipment.setTrackingNumber("FT-2026-" + (int)(1000 + Math.random() * 9000));
        }

        if (shipment.getCreatedAt() == null) {
            shipment.setCreatedAt(LocalDateTime.now());
        }

        if (shipment.getStatus() == null || shipment.getStatus().isBlank()) {
            shipment.setStatus("IN_TRANSIT");
        }

        if (repository.countByTrackingNumber(
                shipment.getTrackingNumber()) > 0) {
            throw new RuntimeException("Tracking number already exists");
        }

        if (shipment.getWeightKg() != null
                && shipment.getWeightKg().signum() <= 0) {
            throw new RuntimeException("Weight must be greater than zero");
        }

        if (shipment.getCreatedAt() == null) {
            shipment.setCreatedAt(LocalDateTime.now());
        }

        if (shipment.getStatus() == null
                || shipment.getStatus().isBlank()) {
            shipment.setStatus("CREATED");
        }

        return repository.save(shipment);
    }

    public Shipment updateShipment(Long id, Shipment input) {

        Shipment existing = getShipmentById(id);

        if (input.getTrackingNumber() == null
                || input.getTrackingNumber().isBlank()
                || input.getPickupAddress() == null
                || input.getPickupAddress().isBlank()
                || input.getDeliveryAddress() == null
                || input.getDeliveryAddress().isBlank()) {
            throw new RuntimeException(
                    "Tracking number and both addresses are required");
        }

        if (repository.countOtherByTrackingNumber(
                input.getTrackingNumber(), id) > 0) {
            throw new RuntimeException("Tracking number already exists");
        }

        if (input.getWeightKg() != null
                && input.getWeightKg().signum() <= 0) {
            throw new RuntimeException("Weight must be greater than zero");
        }

        existing.setTrackingNumber(input.getTrackingNumber());
        existing.setCustomerName(input.getCustomerName());
        existing.setCustomerPhone(input.getCustomerPhone());
        existing.setPickupAddress(input.getPickupAddress());
        existing.setDeliveryAddress(input.getDeliveryAddress());
        existing.setWeightKg(input.getWeightKg());
        existing.setStatus(input.getStatus());
        existing.setExpectedDelivery(input.getExpectedDelivery());

        return repository.save(existing);
    }

    public void deleteShipment(Long id) {
        Shipment shipment = getShipmentById(id);
        repository.delete(shipment);
    }

    @Transactional
    public Shipment updateShipmentStatus(Long id, String newStatus, String remarks) {
        Shipment existing = getShipmentById(id);
        String oldStatus = existing.getStatus();
        
        if (newStatus == null || newStatus.isBlank()) {
            throw new RuntimeException("New status is required");
        }
        String cleanStatus = newStatus.trim().toUpperCase();
        existing.setStatus(cleanStatus);
        Shipment updated = repository.save(existing);

        try {
            Long nextHistId = jdbcTemplate.queryForObject(
                "SELECT NVL(MAX(HISTORY_ID), 0) + 1 FROM SHIPMENT_STATUS_HISTORY", Long.class);
            jdbcTemplate.update(
                "INSERT INTO SHIPMENT_STATUS_HISTORY (HISTORY_ID, SHIPMENT_ID, OLD_STATUS, NEW_STATUS, CHANGED_AT, REMARKS) " +
                "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?)",
                nextHistId, id, oldStatus, cleanStatus, (remarks != null ? remarks : "Status updated to " + cleanStatus)
            );
        } catch (Exception ex) {
            System.err.println("Warning: could not write shipment status history: " + ex.getMessage());
        }

        return updated;
    }

    public List<Map<String, Object>> getShipmentHistory(Long shipmentId) {
        return jdbcTemplate.queryForList(
            "SELECT HISTORY_ID, SHIPMENT_ID, OLD_STATUS, NEW_STATUS, CHANGED_AT, REMARKS " +
            "FROM SHIPMENT_STATUS_HISTORY WHERE SHIPMENT_ID = ? ORDER BY CHANGED_AT DESC",
            shipmentId
        );
    }
}