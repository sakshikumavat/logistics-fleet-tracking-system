
package com.logistics.fleet.controller;

import com.logistics.fleet.entity.RouteStop;
import com.logistics.fleet.service.RouteStopService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route-stops")
public class RouteStopController {

    private final RouteStopService service;

    public RouteStopController(RouteStopService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RouteStop>> getAllStops() {
        return ResponseEntity.ok(service.getAllStops());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteStop> getStopById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getStopById(id));
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<RouteStop>> getStopsByRoute(
            @PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(service.getStopsByRoute(routeId));
    }

    @PostMapping
    public ResponseEntity<RouteStop> createStop(
            @RequestBody RouteStop stop) {
        RouteStop savedStop = service.createStop(stop);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStop);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteStop> updateStop(
            @PathVariable("id") Long id,
            @RequestBody RouteStop stop) {
        return ResponseEntity.ok(service.updateStop(id, stop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStop(
            @PathVariable("id") Long id) {
        service.deleteStop(id);
        return ResponseEntity.noContent().build();
    }
}
