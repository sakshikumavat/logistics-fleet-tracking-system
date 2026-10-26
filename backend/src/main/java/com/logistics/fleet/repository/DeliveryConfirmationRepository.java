
package com.logistics.fleet.repository;

import com.logistics.fleet.entity.DeliveryConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeliveryConfirmationRepository
        extends JpaRepository<DeliveryConfirmation, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
            "FROM DELIVERY_CONFIRMATIONS WHERE SHIPMENT_ID = :shipmentId", nativeQuery = true)
    int countConfirmationByShipmentId(
            @Param("shipmentId") Long shipmentId);

    default boolean existsByShipmentId(Long shipmentId) {
        return countConfirmationByShipmentId(shipmentId) > 0;
    }

    @Query(value = "SELECT * FROM DELIVERY_CONFIRMATIONS " +
            "WHERE SHIPMENT_ID = :shipmentId", nativeQuery = true)
    Optional<DeliveryConfirmation> findByShipmentId(
            @Param("shipmentId") Long shipmentId);
}
