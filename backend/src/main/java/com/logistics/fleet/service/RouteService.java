
package com.logistics.fleet.service;

import com.logistics.fleet.entity.Route;
import com.logistics.fleet.repository.RouteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    // Get all routes
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    // Get route by ID
    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Route not found with ID: " + id));
    }

    // Create route
    public Route createRoute(Route route) {
        if (route.getRouteId() == null) {
            throw new IllegalArgumentException("Route ID is required");
        }

        if (route.getAssignmentId() == null) {
            throw new IllegalArgumentException("Assignment ID is required");
        }

        if (routeRepository.existsById(route.getRouteId())) {
            throw new IllegalArgumentException("Route ID already exists");
        }

        if (route.getStatus() == null || route.getStatus().isBlank()) {
            route.setStatus("PLANNED");
        }

        return routeRepository.save(route);
    }

    // Update route
    public Route updateRoute(Long id, Route updatedRoute) {
        Route existing = getRouteById(id);

        existing.setAssignmentId(updatedRoute.getAssignmentId());
        existing.setRouteName(updatedRoute.getRouteName());
        existing.setStartLocation(updatedRoute.getStartLocation());
        existing.setEndLocation(updatedRoute.getEndLocation());
        existing.setDistanceKm(updatedRoute.getDistanceKm());
        existing.setEstimatedMinutes(updatedRoute.getEstimatedMinutes());
        existing.setStatus(updatedRoute.getStatus());

        return routeRepository.save(existing);
    }

    // Delete route
    public void deleteRoute(Long id) {
        Route existing = getRouteById(id);
        routeRepository.delete(existing);
    }
}
