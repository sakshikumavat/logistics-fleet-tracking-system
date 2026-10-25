
package com.logistics.fleet.service;

import com.logistics.fleet.entity.RouteStop;
import com.logistics.fleet.repository.RouteStopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteStopService {

    private final RouteStopRepository routeStopRepository;

    public RouteStopService(RouteStopRepository routeStopRepository) {
        this.routeStopRepository = routeStopRepository;
    }

    public List<RouteStop> getAllStops() {
        return routeStopRepository.findAll();
    }

    public RouteStop getStopById(Long id) {
        return routeStopRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Route stop not found: " + id));
    }

    public List<RouteStop> getStopsByRoute(Long routeId) {
        return routeStopRepository
                .findByRouteIdOrderByStopOrderAsc(routeId);
    }

    public RouteStop createStop(RouteStop stop) {
        if (stop.getStopId() == null) {
            throw new IllegalArgumentException("Stop ID is required");
        }
        if (stop.getRouteId() == null) {
            throw new IllegalArgumentException("Route ID is required");
        }
        if (stop.getStopOrder() == null) {
            throw new IllegalArgumentException("Stop order is required");
        }
        if (stop.getLocationName() == null
                || stop.getLocationName().isBlank()) {
            throw new IllegalArgumentException("Location name is required");
        }
        if (routeStopRepository.existsById(stop.getStopId())) {
            throw new IllegalArgumentException("Stop ID already exists");
        }

        return routeStopRepository.save(stop);
    }

    public RouteStop updateStop(Long id, RouteStop updatedStop) {
        RouteStop existing = getStopById(id);

        existing.setRouteId(updatedStop.getRouteId());
        existing.setStopOrder(updatedStop.getStopOrder());
        existing.setLocationName(updatedStop.getLocationName());
        existing.setLatitude(updatedStop.getLatitude());
        existing.setLongitude(updatedStop.getLongitude());
        existing.setArrivalTime(updatedStop.getArrivalTime());

        return routeStopRepository.save(existing);
    }

    public void deleteStop(Long id) {
        RouteStop existing = getStopById(id);
        routeStopRepository.delete(existing);
    }
}
