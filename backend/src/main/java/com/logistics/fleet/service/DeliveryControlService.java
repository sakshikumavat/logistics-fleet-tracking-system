
package com.logistics.fleet.service;

import com.logistics.fleet.dto.ShipmentStatusRequest;
import com.logistics.fleet.repository.DeliveryControlRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class DeliveryControlService {

    private final DeliveryControlRepository repository;

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "PENDING",
            "ASSIGNED",
            "IN_TRANSIT",
            "DELIVERED",
            "CANCELLED"
    );

    public DeliveryControlService(
            DeliveryControlRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> getAllShipments() {
        return repository.getAllShipments();
    }

    public Map<String, Object> updateStatus(
            Long shipmentId,
            ShipmentStatusRequest request) {

        if (request == null || request.getStatus() == null
                || request.getStatus().isBlank()) {
            throw new IllegalArgumentException(
                    "Shipment status is required");
        }

        String status = request.getStatus()
                .trim()
                .toUpperCase(Locale.ROOT);

        if (!ALLOWED_STATUSES.contains(status)) {
            throw new IllegalArgumentException(
                    "Invalid shipment status: " + status);
        }

        if (!repository.existsById(shipmentId)) {
            throw new IllegalArgumentException(
                    "Shipment not found: " + shipmentId);
        }

        repository.updateStatus(shipmentId, status);

        return Map.of(
                "message", "Shipment status updated successfully",
                "shipmentId", shipmentId,
                "status", status
        );
    }
}
