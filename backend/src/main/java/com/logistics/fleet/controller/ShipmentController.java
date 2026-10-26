
package com.logistics.fleet.controller;

import com.logistics.fleet.entity.Shipment;
import com.logistics.fleet.entity.DeliveryConfirmation;
import com.logistics.fleet.service.ShipmentService;
import com.logistics.fleet.service.DeliveryConfirmationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService service;
    private final DeliveryConfirmationService deliveryConfirmationService;

    public ShipmentController(
            ShipmentService service,
            DeliveryConfirmationService deliveryConfirmationService) {
        this.service = service;
        this.deliveryConfirmationService = deliveryConfirmationService;
    }

    // Get all shipments
    @GetMapping
    public ResponseEntity<List<Shipment>> getAllShipments() {
        return ResponseEntity.ok(service.getAllShipments());
    }

    // Get shipment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipmentById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getShipmentById(id));
    }

    // Create shipment
    @PostMapping
    public ResponseEntity<Shipment> createShipment(
            @RequestBody Shipment shipment) {

        Shipment savedShipment = service.createShipment(shipment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedShipment);
    }

    // Update shipment details
    @PutMapping("/{id}")
    public ResponseEntity<Shipment> updateShipment(
            @PathVariable("id") Long id,
            @RequestBody Shipment shipment) {

        return ResponseEntity.ok(
                service.updateShipment(id, shipment));
    }

    // Delete shipment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipment(
            @PathVariable("id") Long id) {

        service.deleteShipment(id);

        return ResponseEntity.noContent().build();
    }

    // Update shipment status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Shipment> updateShipmentStatus(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> body) {

        String status = body != null ? body.get("status") : null;
        String remarks = body != null ? body.get("remarks") : null;

        return ResponseEntity.ok(
                service.updateShipmentStatus(id, status, remarks));
    }

    // Get shipment status history
    @GetMapping("/{id}/history")
    public ResponseEntity<List<Map<String, Object>>> getShipmentHistory(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(
                service.getShipmentHistory(id));
    }

    // Confirm delivery
    @PostMapping("/{id}/confirm-delivery")
    public ResponseEntity<DeliveryConfirmation> confirmDelivery(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> body) {

        if (body == null) {
            throw new IllegalArgumentException(
                    "Request body is required");
        }

        DeliveryConfirmation confirmation = deliveryConfirmationService.confirmDelivery(
                id,
                body.get("receiverName"),
                body.get("proofReference"),
                body.get("notes"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(confirmation);
    }
}
