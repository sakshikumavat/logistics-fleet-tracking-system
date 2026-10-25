
package com.logistics.fleet.repository;

import com.logistics.fleet.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MaintenanceRepository
        extends JpaRepository<Maintenance, Long> {

    List<Maintenance> findByVehicleId(Long vehicleId);

    @Query(
        value = "SELECT NVL(SUM(COST), 0) FROM MAINTENANCE WHERE VEHICLE_ID = :vehicleId",
        nativeQuery = true
    )
    BigDecimal getTotalCostByVehicleId(
            @Param("vehicleId") Long vehicleId);
}
