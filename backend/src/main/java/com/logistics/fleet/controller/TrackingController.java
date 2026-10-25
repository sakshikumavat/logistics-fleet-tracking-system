package com.logistics.fleet.controller;

import com.logistics.fleet.dto.ApiResponse;
import com.logistics.fleet.dto.TrackingResponse;
import com.logistics.fleet.service.TrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("/{trackingNumber}")
    public ResponseEntity<ApiResponse<TrackingResponse>> trackShipment(@PathVariable String trackingNumber) {
        TrackingResponse response = trackingService.trackShipment(trackingNumber);
        if (response == null) {
            return ResponseEntity.ok(ApiResponse.error("Shipment not found for tracking number: " + trackingNumber));
        }
        return ResponseEntity.ok(ApiResponse.success("Shipment tracked successfully", response));
    }
}
