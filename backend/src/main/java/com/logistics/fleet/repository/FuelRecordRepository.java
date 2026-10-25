
package com.logistics.fleet.repository;

import com.logistics.fleet.entity.FuelRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FuelRecordRepository
        extends JpaRepository<FuelRecord, Long> {

    List<FuelRecord> findByVehicleIdOrderByFuelDateDesc(
            Long vehicleId);

    @Query(
        value = "SELECT NVL(SUM(TOTAL_COST), 0) " +
                "FROM FUEL_RECORDS WHERE VEHICLE_ID = :vehicleId",
        nativeQuery = true
    )
    BigDecimal getTotalFuelCost(
            @Param("vehicleId") Long vehicleId);
}
