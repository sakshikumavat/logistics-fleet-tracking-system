package com.logistics.fleet.repository;

import com.logistics.fleet.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository
        extends JpaRepository<Assignment, Long> {
}