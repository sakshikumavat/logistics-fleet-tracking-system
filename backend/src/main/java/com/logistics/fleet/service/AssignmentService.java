
package com.logistics.fleet.service;

import com.logistics.fleet.entity.Assignment;
import com.logistics.fleet.repository.AssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class AssignmentService {

    private final AssignmentRepository repository;

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "ASSIGNED", "IN_TRANSIT", "COMPLETED", "CANCELLED");

    private static final Set<String> ACTIVE_STATUSES = Set.of(
            "ASSIGNED", "IN_TRANSIT");

    public AssignmentService(AssignmentRepository repository) {
        this.repository = repository;
    }

    public List<Assignment> getAllAssignments() {
        return repository.findAll();
    }

    public Assignment getAssignmentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found: " + id));
    }

    @Transactional
    public Assignment createAssignment(Assignment assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment data is required");
        }

        if (assignment.getAssignmentId() == null || assignment.getAssignmentId() == 0L) {
            Long maxId = repository.findMaxAssignmentId();
            assignment.setAssignmentId((maxId != null ? maxId : 0L) + 1L);
        }

        validateRequiredIds(assignment);

        if (repository.existsById(assignment.getAssignmentId())) {
            throw new IllegalArgumentException("Assignment ID already exists");
        }

        String status = assignment.getStatus();
        if (status == null || status.isBlank()) {
            status = "ASSIGNED";
        }
        assignment.setStatus(validateStatus(status));

        validateNoConflicts(assignment, null);

        if (assignment.getAssignedAt() == null) {
            assignment.setAssignedAt(LocalDateTime.now());
        }

        return repository.save(assignment);
    }

    @Transactional
    public Assignment updateAssignment(Long id, Assignment input) {
        if (input == null) {
            throw new IllegalArgumentException("Assignment data is required");
        }

        Assignment existing = getAssignmentById(id);
        validateRequiredIds(input);

        String nextStatus = input.getStatus();
        if (nextStatus == null || nextStatus.isBlank()) {
            nextStatus = existing.getStatus();
        }
        nextStatus = validateStatus(nextStatus);

        Assignment candidate = new Assignment();
        candidate.setAssignmentId(id);
        candidate.setShipmentId(input.getShipmentId());
        candidate.setDriverId(input.getDriverId());
        candidate.setVehicleId(input.getVehicleId());
        candidate.setStatus(nextStatus);

        validateNoConflicts(candidate, id);

        existing.setShipmentId(input.getShipmentId());
        existing.setDriverId(input.getDriverId());
        existing.setVehicleId(input.getVehicleId());
        existing.setStatus(nextStatus);

        if (input.getAssignedAt() != null) {
            existing.setAssignedAt(input.getAssignedAt());
        }

        return repository.save(existing);
    }

    private void validateNoConflicts(Assignment assignment, Long excludeId) {
        if (!ACTIVE_STATUSES.contains(assignment.getStatus())) {
            return;
        }

        if (repository.countActiveForDriver(
                assignment.getDriverId(), excludeId) > 0) {
            throw new IllegalArgumentException(
                    "Driver already has another active assignment");
        }

        if (repository.countActiveForVehicle(
                assignment.getVehicleId(), excludeId) > 0) {
            throw new IllegalArgumentException(
                    "Vehicle already has another active assignment");
        }

        if (repository.countActiveForShipment(
                assignment.getShipmentId(), excludeId) > 0) {
            throw new IllegalArgumentException(
                    "Shipment already has another active assignment");
        }
    }

    private void validateRequiredIds(Assignment assignment) {
        if (assignment.getShipmentId() == null
                || assignment.getDriverId() == null
                || assignment.getVehicleId() == null) {
            throw new IllegalArgumentException(
                    "Shipment ID, Driver ID and Vehicle ID are required");
        }
    }

    private String validateStatus(String status) {
        String normalized = status.trim().toUpperCase(Locale.ROOT);

        if (!ALLOWED_STATUSES.contains(normalized)) {
            throw new IllegalArgumentException(
                    "Invalid assignment status: " + status
                            + ". Allowed values: " + ALLOWED_STATUSES);
        }

        return normalized;
    }

    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = getAssignmentById(id);
        repository.delete(assignment);
    }
}
