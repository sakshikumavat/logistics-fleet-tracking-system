
package com.logistics.fleet.controller;

import com.logistics.fleet.entity.Shipment;
import com.logistics.fleet.service.ShipmentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService service;

    public ShipmentController(ShipmentService service) {
        this.service = service;
    }

    // Get all shipments
    @GetMapping
    public ResponseEntity<List<Shipment>> getAllShipments() {
        return ResponseEntity.ok(service.getAllShipments());
    }

    // Get shipment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getShipmentById(id));
    }

    // Create new shipment
    @PostMapping
    public ResponseEntity<Shipment> createShipment(
            @RequestBody Shipment shipment) {

        Shipment savedShipment = service.createShipment(shipment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedShipment);
    }

    // Update existing shipment
    @PutMapping("/{id}")
    public ResponseEntity<Shipment> updateShipment(
            @PathVariable Long id,
            @RequestBody Shipment shipment) {

        return ResponseEntity.ok(
                service.updateShipment(id, shipment)
        );
    }

    // Delete shipment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipment(
            @PathVariable Long id) {

        service.deleteShipment(id);

        return ResponseEntity.noContent().build();
    }

    // Update shipment status (PATCH)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Shipment> updateShipmentStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        String status = body != null ? body.get("status") : null;
        String remarks = body != null ? body.get("remarks") : null;
        return ResponseEntity.ok(service.updateShipmentStatus(id, status, remarks));
    }

    // Get shipment status history (GET)
    @GetMapping("/{id}/history")
    public ResponseEntity<List<java.util.Map<String, Object>>> getShipmentHistory(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getShipmentHistory(id));
    }
}
