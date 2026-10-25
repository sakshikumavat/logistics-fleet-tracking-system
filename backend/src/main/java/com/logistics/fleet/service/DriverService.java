
package com.logistics.fleet.service;

import com.logistics.fleet.entity.Driver;
import com.logistics.fleet.repository.DriverRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository repository;

    public DriverService(DriverRepository repository) {
        this.repository = repository;
    }

    // Get all drivers
    public List<Driver> getAllDrivers() {
        return repository.findAll();
    }

    // Get driver by ID
    public Driver getDriverById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Driver not found: " + id
                ));
    }

    // Create driver
    @Transactional
    public Driver createDriver(Driver driver) {

        if (driver.getDriverId() == null || driver.getDriverId() == 0L) {
            Long maxId = repository.findMaxDriverId();
            driver.setDriverId((maxId != null ? maxId : 0L) + 1L);
        }

        if (driver.getFullName() == null ||
                driver.getFullName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Full name is required"
            );
        }

        if (driver.getPhone() == null ||
                driver.getPhone().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Phone is required"
            );
        }

        if (driver.getLicenseNumber() == null ||
                driver.getLicenseNumber().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "License number is required"
            );
        }

        if (repository.existsById(driver.getDriverId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Driver ID already exists"
            );
        }

        if (repository.countByPhone(driver.getPhone()) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Phone number already exists"
            );
        }

        if (repository.countByLicenseNumber(
                driver.getLicenseNumber()) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "License number already exists"
            );
        }

        return repository.save(driver);
    }

    // Update driver
    @Transactional
    public Driver updateDriver(Long id, Driver input) {

        Driver existing = getDriverById(id);

        if (input.getFullName() == null ||
                input.getFullName().isBlank() ||
                input.getPhone() == null ||
                input.getPhone().isBlank() ||
                input.getLicenseNumber() == null ||
                input.getLicenseNumber().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Full name, phone and license number are required"
            );
        }

        // Check phone against other drivers, excluding current ID
        if (repository.countOtherByPhone(input.getPhone(), id) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Phone number already exists"
            );
        }

        // Check license against other drivers, excluding current ID
        if (repository.countOtherByLicenseNumber(
                input.getLicenseNumber(), id) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "License number already exists"
            );
        }

        existing.setFullName(input.getFullName());
        existing.setPhone(input.getPhone());
        existing.setLicenseNumber(input.getLicenseNumber());
        existing.setLicenseExpiry(input.getLicenseExpiry());
        existing.setStatus(input.getStatus());

        return repository.save(existing);
    }

    // Delete driver
    @Transactional
    public void deleteDriver(Long id) {
        Driver driver = getDriverById(id);
        repository.delete(driver);
    }
}
