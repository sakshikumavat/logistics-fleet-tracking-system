
package com.logistics.fleet.service;

import com.logistics.fleet.dto.TrackingResponse;
import com.logistics.fleet.repository.TrackingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TrackingService {

    private final TrackingRepository repository;

    public TrackingService(TrackingRepository repository) {
        this.repository = repository;
    }

    public TrackingResponse trackShipment(String trackingNumber) {

        List<Map<String, Object>> shipments =
                repository.findShipment(trackingNumber);

        if (shipments.isEmpty()) {
            return null;
        }

        List<Map<String, Object>> stops =
                repository.findStops(trackingNumber);

        return new TrackingResponse(shipments.get(0), stops);
    }
}
