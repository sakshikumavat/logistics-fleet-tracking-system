
package com.logistics.fleet.controller;

import com.logistics.fleet.dto.ShipmentStatusRequest;
import com.logistics.fleet.service.DeliveryControlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/delivery-control")
public class DeliveryControlController {

    private final DeliveryControlService service;

    public DeliveryControlController(
            DeliveryControlService service) {
        this.service = service;
    }

    @GetMapping("/shipments")
    public List<Map<String, Object>> getAllShipments() {
        return service.getAllShipments();
    }

    @PutMapping("/shipments/{shipmentId}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long shipmentId,
            @RequestBody ShipmentStatusRequest request) {

        return ResponseEntity.ok(
                service.updateStatus(shipmentId, request));
    }
}
