
package com.logistics.fleet.repository;

import com.logistics.fleet.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignmentRepository
                extends JpaRepository<Assignment, Long> {

        @Query("""
                        SELECT COUNT(a)
                        FROM Assignment a
                        WHERE a.driverId = :driverId
                          AND UPPER(a.status) IN ('ASSIGNED', 'IN_TRANSIT')
                          AND (:excludeId IS NULL OR a.assignmentId <> :excludeId)
                        """)
        long countActiveForDriver(
                        @Param("driverId") Long driverId,
                        @Param("excludeId") Long excludeId);

        @Query("""
                        SELECT COUNT(a)
                        FROM Assignment a
                        WHERE a.vehicleId = :vehicleId
                          AND UPPER(a.status) IN ('ASSIGNED', 'IN_TRANSIT')
                          AND (:excludeId IS NULL OR a.assignmentId <> :excludeId)
                        """)
        long countActiveForVehicle(
                        @Param("vehicleId") Long vehicleId,
                        @Param("excludeId") Long excludeId);

        @Query("""
                        SELECT COUNT(a)
                        FROM Assignment a
                        WHERE a.shipmentId = :shipmentId
                          AND UPPER(a.status) IN ('ASSIGNED', 'IN_TRANSIT')
                          AND (:excludeId IS NULL OR a.assignmentId <> :excludeId)
                        """)
        long countActiveForShipment(
                        @Param("shipmentId") Long shipmentId,
                        @Param("excludeId") Long excludeId);

        @Query("SELECT COALESCE(MAX(a.assignmentId), 0) FROM Assignment a")
        Long findMaxAssignmentId();
}
