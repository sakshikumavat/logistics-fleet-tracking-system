
package com.logistics.fleet.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DeliveryControlRepository {

    private final JdbcTemplate jdbcTemplate;

    public DeliveryControlRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getAllShipments() {
        String sql =
                "SELECT SHIPMENT_ID, TRACKING_NUMBER, CUSTOMER_NAME, " +
                "CUSTOMER_PHONE, PICKUP_ADDRESS, DELIVERY_ADDRESS, " +
                "STATUS, CREATED_AT, EXPECTED_DELIVERY " +
                "FROM SHIPMENTS ORDER BY SHIPMENT_ID DESC";

        return jdbcTemplate.queryForList(sql);
    }

    public boolean existsById(Long shipmentId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM SHIPMENTS WHERE SHIPMENT_ID = ?",
                Integer.class,
                shipmentId
        );

        return count != null && count > 0;
    }

    public int updateStatus(Long shipmentId, String status) {
        return jdbcTemplate.update(
                "UPDATE SHIPMENTS SET STATUS = ? WHERE SHIPMENT_ID = ?",
                status,
                shipmentId
        );
    }
}
