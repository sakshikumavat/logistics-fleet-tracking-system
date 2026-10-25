
package com.logistics.fleet.controller;

import com.logistics.fleet.entity.Vehicle;
import com.logistics.fleet.service.VehicleService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return service.getAllVehicles();
    }

    @GetMapping("/{id}")
    public Vehicle getVehicleById(@PathVariable Long id) {
        return service.getVehicleById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle createVehicle(@Valid @RequestBody Vehicle vehicle) {
        return service.createVehicle(vehicle);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody Vehicle vehicle) {
        return service.updateVehicle(id, vehicle);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(@PathVariable Long id) {
        service.deleteVehicle(id);
    }

    // Record vehicle location
    @PostMapping("/{id}/locations")
    @ResponseStatus(HttpStatus.CREATED)
    public java.util.Map<String, Object> recordLocation(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Object> body) {
        Double lat = body.get("latitude") != null ? Double.valueOf(body.get("latitude").toString()) : null;
        Double lng = body.get("longitude") != null ? Double.valueOf(body.get("longitude").toString()) : null;
        return service.recordLocation(id, lat, lng);
    }

    // Get latest vehicle location
    @GetMapping("/{id}/locations/latest")
    public java.util.Map<String, Object> getLatestLocation(@PathVariable Long id) {
        return service.getLatestLocation(id);
    }
}
