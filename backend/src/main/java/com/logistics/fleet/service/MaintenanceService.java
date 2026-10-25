
package com.logistics.fleet.service;

import com.logistics.fleet.entity.Maintenance;
import com.logistics.fleet.repository.MaintenanceRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceService(
            MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    public List<Maintenance> getAllMaintenance() {
        return maintenanceRepository.findAll();
    }

    public Maintenance getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Maintenance record not found: " + id));
    }

    public List<Maintenance> getMaintenanceByVehicleId(
            Long vehicleId) {
        return maintenanceRepository.findByVehicleId(vehicleId);
    }

    public BigDecimal getTotalCostByVehicleId(Long vehicleId) {
        return maintenanceRepository.getTotalCostByVehicleId(vehicleId);
    }

    public Maintenance createMaintenance(Maintenance maintenance) {

        if (maintenance.getMaintenanceId() == null) {
            throw new IllegalArgumentException(
                    "Maintenance ID is required");
        }

        if (maintenance.getVehicleId() == null) {
            throw new IllegalArgumentException(
                    "Vehicle ID is required");
        }

        if (maintenance.getMaintenanceDate() == null) {
            throw new IllegalArgumentException(
                    "Maintenance date is required");
        }

        if (maintenance.getDescription() == null
                || maintenance.getDescription().isBlank()) {
            throw new IllegalArgumentException(
                    "Description is required");
        }

        if (maintenance.getCost() == null
                || maintenance.getCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Cost must be zero or greater");
        }

        if (maintenance.getMaintenanceStatus() == null
                || maintenance.getMaintenanceStatus().isBlank()) {
            throw new IllegalArgumentException(
                    "Maintenance status is required");
        }

        if (maintenanceRepository.existsById(
                maintenance.getMaintenanceId())) {
            throw new IllegalArgumentException(
                    "Maintenance ID already exists");
        }

        return maintenanceRepository.save(maintenance);
    }

    public Maintenance updateMaintenance(
            Long id, Maintenance updatedMaintenance) {

        Maintenance existing = getMaintenanceById(id);

        existing.setVehicleId(updatedMaintenance.getVehicleId());
        existing.setMaintenanceDate(
                updatedMaintenance.getMaintenanceDate());
        existing.setDescription(updatedMaintenance.getDescription());
        existing.setCost(updatedMaintenance.getCost());
        existing.setMaintenanceStatus(
                updatedMaintenance.getMaintenanceStatus());

        return maintenanceRepository.save(existing);
    }

    public void deleteMaintenance(Long id) {
        Maintenance existing = getMaintenanceById(id);
        maintenanceRepository.delete(existing);
    }
}
