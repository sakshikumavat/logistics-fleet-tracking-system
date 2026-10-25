package com.logistics.fleet.repository;

import com.logistics.fleet.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query(
        value = "SELECT COUNT(*) FROM DRIVERS WHERE PHONE = :phone",
        nativeQuery = true
    )
    long countByPhone(@Param("phone") String phone);

    @Query(
        value = "SELECT COUNT(*) FROM DRIVERS WHERE LICENSE_NUMBER = :licenseNumber",
        nativeQuery = true
    )
    long countByLicenseNumber(
        @Param("licenseNumber") String licenseNumber
    );

    @Query(
        value = "SELECT COUNT(*) FROM DRIVERS " +
                "WHERE PHONE = :phone AND DRIVER_ID <> :driverId",
        nativeQuery = true
    )
    long countOtherByPhone(
        @Param("phone") String phone,
        @Param("driverId") Long driverId
    );

    @Query(
        value = "SELECT COUNT(*) FROM DRIVERS " +
                "WHERE LICENSE_NUMBER = :licenseNumber " +
                "AND DRIVER_ID <> :driverId",
        nativeQuery = true
    )
    long countOtherByLicenseNumber(
        @Param("licenseNumber") String licenseNumber,
        @Param("driverId") Long driverId
    );

    @Query("SELECT COALESCE(MAX(d.driverId), 0) FROM Driver d")
    Long findMaxDriverId();
}
