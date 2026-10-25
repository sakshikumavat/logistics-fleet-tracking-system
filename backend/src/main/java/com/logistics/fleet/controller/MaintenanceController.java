
package com.logistics.fleet.controller;

import com.logistics.fleet.entity.Maintenance;
import com.logistics.fleet.service.MaintenanceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(
            MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping
    public ResponseEntity<List<Maintenance>> getAllMaintenance() {
        return ResponseEntity.ok(
                maintenanceService.getAllMaintenance());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Maintenance> getMaintenanceById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(
                maintenanceService.getMaintenanceById(id));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<Maintenance>> getByVehicleId(
            @PathVariable("vehicleId") Long vehicleId) {
        return ResponseEntity.ok(
                maintenanceService.getMaintenanceByVehicleId(vehicleId));
    }

    @GetMapping("/vehicle/{vehicleId}/total-cost")
    public ResponseEntity<BigDecimal> getTotalCost(
            @PathVariable("vehicleId") Long vehicleId) {
        return ResponseEntity.ok(
                maintenanceService.getTotalCostByVehicleId(vehicleId));
    }

    @PostMapping
    public ResponseEntity<Maintenance> createMaintenance(
            @RequestBody Maintenance maintenance) {
        Maintenance saved =
                maintenanceService.createMaintenance(maintenance);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Maintenance> updateMaintenance(
            @PathVariable("id") Long id,
            @RequestBody Maintenance maintenance) {
        return ResponseEntity.ok(
                maintenanceService.updateMaintenance(id, maintenance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(
            @PathVariable("id") Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }
}
