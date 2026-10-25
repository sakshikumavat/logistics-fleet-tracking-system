package com.logistics.fleet.repository;

import com.logistics.fleet.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Oracle 11g compatible duplicate registration check
    @Query(
        value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
                "FROM VEHICLES WHERE REGISTRATION_NO = :registrationNo",
        nativeQuery = true
    )
    int registrationExists(
        @Param("registrationNo") String registrationNo
    );

    // Find vehicle by registration number
    Optional<Vehicle> findByRegistrationNo(String registrationNo);

    java.util.List<Vehicle> findByVehicleStatus(String vehicleStatus);

    @Query("SELECT COALESCE(MAX(v.vehicleId), 0) FROM Vehicle v")
    Long findMaxVehicleId();
}
