
package com.logistics.fleet.controller;

import com.logistics.fleet.entity.Route;
import com.logistics.fleet.service.RouteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @PostMapping
    public ResponseEntity<Route> createRoute(
            @RequestBody Route route) {
        Route savedRoute = routeService.createRoute(route);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoute);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Route> updateRoute(
            @PathVariable("id") Long id,
            @RequestBody Route route) {
        return ResponseEntity.ok(routeService.updateRoute(id, route));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(
            @PathVariable("id") Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
