package com.logistics.fleet.repository;

import com.logistics.fleet.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Query(value = """
        SELECT COUNT(*)
        FROM SHIPMENTS
        WHERE TRACKING_NUMBER = :trackingNumber
        """, nativeQuery = true)
    long countByTrackingNumber(
            @Param("trackingNumber") String trackingNumber);

    @Query(value = """
        SELECT COUNT(*)
        FROM SHIPMENTS
        WHERE TRACKING_NUMBER = :trackingNumber
          AND SHIPMENT_ID <> :shipmentId
        """, nativeQuery = true)
    long countOtherByTrackingNumber(
            @Param("trackingNumber") String trackingNumber,
            @Param("shipmentId") Long shipmentId);

    @Query("SELECT COALESCE(MAX(s.shipmentId), 0) FROM Shipment s")
    Long findMaxShipmentId();
}