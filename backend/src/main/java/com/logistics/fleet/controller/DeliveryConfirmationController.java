
package com.logistics.fleet.controller;

import com.logistics.fleet.entity.DeliveryConfirmation;
import com.logistics.fleet.service.DeliveryConfirmationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
public class DeliveryConfirmationController {

    private final DeliveryConfirmationService deliveryConfirmationService;

    public DeliveryConfirmationController(
            DeliveryConfirmationService deliveryConfirmationService) {
        this.deliveryConfirmationService = deliveryConfirmationService;
    }

    // Get delivery confirmation by shipment ID
    @GetMapping("/{id}/confirmation")
    public ResponseEntity<DeliveryConfirmation> getDeliveryConfirmation(
            @PathVariable("id") Long id) {

        DeliveryConfirmation confirmation = deliveryConfirmationService.getConfirmation(id);

        return ResponseEntity.ok(confirmation);
    }
}
