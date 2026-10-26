
package com.logistics.fleet.service;

import com.logistics.fleet.entity.Shipment;
import com.logistics.fleet.repository.ShipmentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ShipmentService {

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "CREATED",
            "ASSIGNED",
            "IN_TRANSIT",
            "DELIVERED",
            "CANCELLED");

    private final ShipmentRepository repository;
    private final JdbcTemplate jdbcTemplate;

    public ShipmentService(
            ShipmentRepository repository,
            JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Shipment> getAllShipments() {
        return repository.findAll();
    }

    public Shipment getShipmentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + id));
    }

    @Transactional
    public Shipment createShipment(Shipment shipment) {

        if (shipment.getShipmentId() == null
                || shipment.getShipmentId() == 0L) {
            Long maxId = repository.findMaxShipmentId();
            shipment.setShipmentId(
                    (maxId != null ? maxId : 0L) + 1L);
        }

        if (shipment.getTrackingNumber() == null
                || shipment.getTrackingNumber().isBlank()) {
            shipment.setTrackingNumber(
                    "FT-" + System.currentTimeMillis());
        }

        if (repository.countByTrackingNumber(
                shipment.getTrackingNumber()) > 0) {
            throw new RuntimeException(
                    "Tracking number already exists");
        }

        if (shipment.getPickupAddress() == null
                || shipment.getPickupAddress().isBlank()
                || shipment.getDeliveryAddress() == null
                || shipment.getDeliveryAddress().isBlank()) {
            throw new RuntimeException(
                    "Pickup and delivery addresses are required");
        }

        if (shipment.getWeightKg() != null
                && shipment.getWeightKg().signum() <= 0) {
            throw new RuntimeException(
                    "Weight must be greater than zero");
        }

        if (shipment.getCreatedAt() == null) {
            shipment.setCreatedAt(LocalDateTime.now());
        }

        String status = shipment.getStatus();

        if (status == null || status.isBlank()) {
            status = "CREATED";
        } else {
            status = status.trim().toUpperCase();
        }

        validateStatus(status);
        shipment.setStatus(status);

        return repository.save(shipment);
    }

    @Transactional
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
            throw new RuntimeException(
                    "Tracking number already exists");
        }

        if (input.getWeightKg() != null
                && input.getWeightKg().signum() <= 0) {
            throw new RuntimeException(
                    "Weight must be greater than zero");
        }

        existing.setTrackingNumber(input.getTrackingNumber());
        existing.setCustomerName(input.getCustomerName());
        existing.setCustomerPhone(input.getCustomerPhone());
        existing.setPickupAddress(input.getPickupAddress());
        existing.setDeliveryAddress(input.getDeliveryAddress());
        existing.setWeightKg(input.getWeightKg());
        existing.setExpectedDelivery(input.getExpectedDelivery());

        // Status changes must go through updateShipmentStatus()
        // so that shipment history is recorded.

        return repository.save(existing);
    }

    @Transactional
    public void deleteShipment(Long id) {
        Shipment shipment = getShipmentById(id);
        repository.delete(shipment);
    }

    @Transactional
    public Shipment updateShipmentStatus(
            Long id, String newStatus, String remarks) {

        Shipment existing = getShipmentById(id);

        if (newStatus == null || newStatus.isBlank()) {
            throw new RuntimeException("New status is required");
        }

        String cleanStatus = newStatus.trim().toUpperCase();
        validateStatus(cleanStatus);

        String oldStatus = existing.getStatus();

        if (cleanStatus.equalsIgnoreCase(oldStatus)) {
            return existing;
        }

        if ("DELIVERED".equalsIgnoreCase(oldStatus)
                || "CANCELLED".equalsIgnoreCase(oldStatus)) {
            throw new RuntimeException(
                    "Cannot change status from " + oldStatus);
        }

        existing.setStatus(cleanStatus);
        Shipment updated = repository.save(existing);

        Long nextHistoryId = jdbcTemplate.queryForObject(
                "SELECT NVL(MAX(HISTORY_ID), 0) + 1 " +
                        "FROM SHIPMENT_STATUS_HISTORY",
                Long.class);

        if (nextHistoryId == null) {
            throw new RuntimeException(
                    "Could not generate shipment history ID");
        }

        String historyRemarks = (remarks == null || remarks.isBlank())
                ? "Status changed to " + cleanStatus
                : remarks.trim();

        jdbcTemplate.update(
                "INSERT INTO SHIPMENT_STATUS_HISTORY " +
                        "(HISTORY_ID, SHIPMENT_ID, OLD_STATUS, NEW_STATUS, " +
                        "CHANGED_AT, REMARKS) " +
                        "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?)",
                nextHistoryId,
                id,
                oldStatus,
                cleanStatus,
                historyRemarks);

        return updated;
    }

    public List<Map<String, Object>> getShipmentHistory(
            Long shipmentId) {

        getShipmentById(shipmentId);

        return jdbcTemplate.queryForList(
                "SELECT HISTORY_ID, SHIPMENT_ID, OLD_STATUS, " +
                        "NEW_STATUS, CHANGED_AT, REMARKS " +
                        "FROM SHIPMENT_STATUS_HISTORY " +
                        "WHERE SHIPMENT_ID = ? ORDER BY CHANGED_AT DESC",
                shipmentId);
    }

    private void validateStatus(String status) {
        if (!ALLOWED_STATUSES.contains(status)) {
            throw new RuntimeException(
                    "Invalid shipment status: " + status +
                            ". Allowed statuses: " + ALLOWED_STATUSES);
        }
    }
}
