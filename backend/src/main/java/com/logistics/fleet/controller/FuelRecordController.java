
package com.logistics.fleet.controller;

import com.logistics.fleet.entity.FuelRecord;
import com.logistics.fleet.service.FuelRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/fuel")
public class FuelRecordController {

    private final FuelRecordService fuelRecordService;

    public FuelRecordController(
            FuelRecordService fuelRecordService) {
        this.fuelRecordService = fuelRecordService;
    }

    @GetMapping
    public ResponseEntity<List<FuelRecord>> getAllFuelRecords() {
        return ResponseEntity.ok(
                fuelRecordService.getAllFuelRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelRecord> getFuelRecordById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(
                fuelRecordService.getFuelRecordById(id));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<FuelRecord>> getByVehicle(
            @PathVariable("vehicleId") Long vehicleId) {
        return ResponseEntity.ok(
                fuelRecordService.getFuelRecordsByVehicle(vehicleId));
    }

    @GetMapping("/vehicle/{vehicleId}/total-cost")
    public ResponseEntity<BigDecimal> getTotalFuelCost(
            @PathVariable("vehicleId") Long vehicleId) {
        return ResponseEntity.ok(
                fuelRecordService.getTotalFuelCost(vehicleId));
    }

    @PostMapping
    public ResponseEntity<FuelRecord> createFuelRecord(
            @RequestBody FuelRecord record) {
        FuelRecord saved =
                fuelRecordService.createFuelRecord(record);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuelRecord> updateFuelRecord(
            @PathVariable("id") Long id,
            @RequestBody FuelRecord record) {
        return ResponseEntity.ok(
                fuelRecordService.updateFuelRecord(id, record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuelRecord(
            @PathVariable("id") Long id) {
        fuelRecordService.deleteFuelRecord(id);
        return ResponseEntity.noContent().build();
    }
}
