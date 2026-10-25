package com.logistics.fleet.service;

import com.logistics.fleet.entity.Assignment;
import com.logistics.fleet.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository repository;

    public AssignmentService(AssignmentRepository repository) {
        this.repository = repository;
    }

    public List<Assignment> getAllAssignments() {
        return repository.findAll();
    }

    public Assignment getAssignmentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Assignment not found: " + id));
    }

    public Assignment createAssignment(Assignment assignment) {

        if (assignment.getAssignmentId() == null) {
            throw new RuntimeException("Assignment ID is required");
        }

        if (assignment.getShipmentId() == null
                || assignment.getDriverId() == null
                || assignment.getVehicleId() == null) {
            throw new RuntimeException(
                    "Shipment ID, Driver ID and Vehicle ID are required");
        }

        if (repository.existsById(assignment.getAssignmentId())) {
            throw new RuntimeException("Assignment ID already exists");
        }

        if (assignment.getAssignedAt() == null) {
            assignment.setAssignedAt(LocalDateTime.now());
        }

        if (assignment.getStatus() == null
                || assignment.getStatus().isBlank()) {
            assignment.setStatus("ASSIGNED");
        }

        return repository.save(assignment);
    }

    public Assignment updateAssignment(Long id, Assignment input) {

        Assignment existing = getAssignmentById(id);

        if (input.getShipmentId() == null
                || input.getDriverId() == null
                || input.getVehicleId() == null) {
            throw new RuntimeException(
                    "Shipment ID, Driver ID and Vehicle ID are required");
        }

        existing.setShipmentId(input.getShipmentId());
        existing.setDriverId(input.getDriverId());
        existing.setVehicleId(input.getVehicleId());

        if (input.getAssignedAt() != null) {
            existing.setAssignedAt(input.getAssignedAt());
        }

        if (input.getStatus() != null && !input.getStatus().isBlank()) {
            existing.setStatus(input.getStatus());
        }

        return repository.save(existing);
    }

    public void deleteAssignment(Long id) {
        Assignment assignment = getAssignmentById(id);
        repository.delete(assignment);
    }
}