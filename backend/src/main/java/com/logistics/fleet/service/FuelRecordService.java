
package com.logistics.fleet.service;

import com.logistics.fleet.entity.FuelRecord;
import com.logistics.fleet.repository.FuelRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FuelRecordService {

    private final FuelRecordRepository fuelRecordRepository;

    public FuelRecordService(
            FuelRecordRepository fuelRecordRepository) {
        this.fuelRecordRepository = fuelRecordRepository;
    }

    public List<FuelRecord> getAllFuelRecords() {
        return fuelRecordRepository.findAll();
    }

    public FuelRecord getFuelRecordById(Long id) {
        return fuelRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Fuel record not found: " + id));
    }

    public List<FuelRecord> getFuelRecordsByVehicle(Long vehicleId) {
        return fuelRecordRepository
                .findByVehicleIdOrderByFuelDateDesc(vehicleId);
    }

    public BigDecimal getTotalFuelCost(Long vehicleId) {
        return fuelRecordRepository.getTotalFuelCost(vehicleId);
    }

    public FuelRecord createFuelRecord(FuelRecord record) {
        validate(record);

        if (fuelRecordRepository.existsById(record.getFuelId())) {
            throw new IllegalArgumentException(
                    "Fuel ID already exists");
        }

        calculateTotalCost(record);
        return fuelRecordRepository.save(record);
    }

    public FuelRecord updateFuelRecord(
            Long id, FuelRecord updatedRecord) {
        FuelRecord existing = getFuelRecordById(id);

        validate(updatedRecord);

        existing.setVehicleId(updatedRecord.getVehicleId());
        existing.setFuelDate(updatedRecord.getFuelDate());
        existing.setFuelType(updatedRecord.getFuelType());
        existing.setLitres(updatedRecord.getLitres());
        existing.setPricePerLitre(
                updatedRecord.getPricePerLitre());
        existing.setOdometerReading(
                updatedRecord.getOdometerReading());
        existing.setFuelStation(updatedRecord.getFuelStation());

        calculateTotalCost(existing);
        return fuelRecordRepository.save(existing);
    }

    public void deleteFuelRecord(Long id) {
        FuelRecord existing = getFuelRecordById(id);
        fuelRecordRepository.delete(existing);
    }

    private void validate(FuelRecord record) {
        if (record.getFuelId() == null
                || record.getVehicleId() == null) {
            throw new IllegalArgumentException(
                    "Fuel ID and Vehicle ID are required");
        }

        if (record.getFuelDate() == null) {
            throw new IllegalArgumentException(
                    "Fuel date is required");
        }

        if (record.getFuelType() == null
                || record.getFuelType().isBlank()) {
            throw new IllegalArgumentException(
                    "Fuel type is required");
        }

        if (record.getLitres() == null
                || record.getLitres()
                        .compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Litres must be greater than zero");
        }

        if (record.getPricePerLitre() == null
                || record.getPricePerLitre()
                        .compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Price per litre cannot be negative");
        }
    }

    private void calculateTotalCost(FuelRecord record) {
        BigDecimal total = record.getLitres()
                .multiply(record.getPricePerLitre());

        record.setTotalCost(total);
    }
}
