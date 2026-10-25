
package com.logistics.fleet.controller;

import com.logistics.fleet.entity.Driver;
import com.logistics.fleet.service.DriverService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService service;

    public DriverController(DriverService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(service.getAllDrivers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDriverById(id));
    }

    @PostMapping
    public ResponseEntity<Driver> createDriver(
            @RequestBody Driver driver) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createDriver(driver));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateDriver(
            @PathVariable Long id,
            @RequestBody Driver driver) {
        return ResponseEntity.ok(service.updateDriver(id, driver));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        service.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }
}
