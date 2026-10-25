
package com.logistics.fleet.service;

import com.logistics.fleet.entity.Vehicle;
import com.logistics.fleet.repository.VehicleRepository;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class VehicleService {

    private final VehicleRepository repository;
    private final JdbcTemplate jdbcTemplate;

    public VehicleService(VehicleRepository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // Get all vehicles
    public List<Vehicle> getAllVehicles() {
        return repository.findAll();
    }

    // Get vehicle by ID
    public Vehicle getVehicleById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vehicle not found with ID: " + id
                ));
    }

    // Create vehicle
    @Transactional
    public Vehicle createVehicle(Vehicle vehicle) {

        if (vehicle.getVehicleId() == null || vehicle.getVehicleId() == 0L) {
            Long maxId = repository.findMaxVehicleId();
            vehicle.setVehicleId((maxId != null ? maxId : 0L) + 1L);
        }

        if (vehicle.getRegistrationNo() == null ||
                vehicle.getRegistrationNo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Registration number is required"
            );
        }

        // Oracle 11g compatible duplicate check
        if (repository.registrationExists(
                vehicle.getRegistrationNo()) == 1) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Registration number already exists"
            );
        }

        if (vehicle.getVehicleName() == null ||
                vehicle.getVehicleName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vehicle name is required"
            );
        }

        if (vehicle.getVehicleType() == null ||
                vehicle.getVehicleType().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vehicle type is required"
            );
        }

        if (vehicle.getDailyRate() == null ||
                vehicle.getDailyRate().signum() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Daily rate must be greater than zero"
            );
        }

        if (vehicle.getVehicleStatus() == null ||
                !List.of("AVAILABLE", "RENTED", "MAINTENANCE", "ON_ROUTE")
                        .contains(vehicle.getVehicleStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status must be AVAILABLE, RENTED, MAINTENANCE, or ON_ROUTE"
            );
        }

        return repository.save(vehicle);
    }

    // Update vehicle
    @Transactional
    public Vehicle updateVehicle(Long id, Vehicle input) {

        Vehicle existing = getVehicleById(id);

        if (input.getRegistrationNo() == null ||
                input.getRegistrationNo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Registration number is required"
            );
        }

        repository.findByRegistrationNo(input.getRegistrationNo())
                .filter(v -> !v.getVehicleId().equals(id))
                .ifPresent(v -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Registration number already exists"
                    );
                });

        if (input.getVehicleName() == null ||
                input.getVehicleName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vehicle name is required"
            );
        }

        if (input.getVehicleType() == null ||
                input.getVehicleType().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vehicle type is required"
            );
        }

        if (input.getDailyRate() == null ||
                input.getDailyRate().signum() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Daily rate must be greater than zero"
            );
        }

        if (input.getVehicleStatus() == null ||
                !List.of("AVAILABLE", "RENTED", "MAINTENANCE", "ON_ROUTE")
                        .contains(input.getVehicleStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status must be AVAILABLE, RENTED, MAINTENANCE, or ON_ROUTE"
            );
        }

        existing.setRegistrationNo(input.getRegistrationNo());
        existing.setVehicleName(input.getVehicleName());
        existing.setVehicleType(input.getVehicleType());
        existing.setDailyRate(input.getDailyRate());
        existing.setVehicleStatus(input.getVehicleStatus());

        return repository.save(existing);
    }

    // Delete vehicle
    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        repository.delete(vehicle);
    }

    // Record vehicle location
    @Transactional
    public Map<String, Object> recordLocation(Long vehicleId, Double latitude, Double longitude) {
        getVehicleById(vehicleId); // verify vehicle exists
        if (latitude == null || longitude == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Latitude and Longitude are required");
        }
        Long nextLocId = jdbcTemplate.queryForObject(
            "SELECT NVL(MAX(LOCATION_ID), 0) + 1 FROM VEHICLE_LOCATIONS", Long.class);
        jdbcTemplate.update(
            "INSERT INTO VEHICLE_LOCATIONS (LOCATION_ID, VEHICLE_ID, LATITUDE, LONGITUDE, RECORDED_AT) " +
            "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)",
            nextLocId, vehicleId, latitude, longitude
        );
        return Map.of(
            "locationId", nextLocId,
            "vehicleId", vehicleId,
            "latitude", latitude,
            "longitude", longitude,
            "message", "Location recorded successfully"
        );
    }

    // Get latest vehicle location
    public Map<String, Object> getLatestLocation(Long vehicleId) {
        getVehicleById(vehicleId); // verify vehicle exists
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
            "SELECT LOCATION_ID, VEHICLE_ID, LATITUDE, LONGITUDE, RECORDED_AT " +
            "FROM VEHICLE_LOCATIONS WHERE VEHICLE_ID = ? ORDER BY RECORDED_AT DESC",
            vehicleId
        );
        if (list.isEmpty()) {
            return Map.of("vehicleId", vehicleId, "message", "No telemetry data recorded yet for this vehicle");
        }
        return list.get(0);
    }
}
