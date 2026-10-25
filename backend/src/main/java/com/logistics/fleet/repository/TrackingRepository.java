
package com.logistics.fleet.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TrackingRepository {

    private final JdbcTemplate jdbcTemplate;

    public TrackingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> findShipment(
            String trackingNumber) {

        String sql =
                "SELECT S.SHIPMENT_ID, S.TRACKING_NUMBER, " +
                "S.CUSTOMER_NAME, S.PICKUP_ADDRESS, " +
                "S.DELIVERY_ADDRESS, S.STATUS, S.CREATED_AT, " +
                "S.EXPECTED_DELIVERY, A.ASSIGNMENT_ID, " +
                "A.STATUS AS ASSIGNMENT_STATUS, " +
                "V.REGISTRATION_NO, V.VEHICLE_NAME, " +
                "D.FULL_NAME AS DRIVER_NAME, " +
                "R.ROUTE_ID, R.ROUTE_NAME, R.START_LOCATION, " +
                "R.END_LOCATION, R.DISTANCE_KM, R.ESTIMATED_MINUTES " +
                "FROM SHIPMENTS S " +
                "LEFT JOIN ASSIGNMENTS A " +
                "ON S.SHIPMENT_ID = A.SHIPMENT_ID " +
                "LEFT JOIN VEHICLES V " +
                "ON A.VEHICLE_ID = V.VEHICLE_ID " +
                "LEFT JOIN DRIVERS D " +
                "ON A.DRIVER_ID = D.DRIVER_ID " +
                "LEFT JOIN ROUTES R " +
                "ON A.ASSIGNMENT_ID = R.ASSIGNMENT_ID " +
                "WHERE S.TRACKING_NUMBER = ?";

        return jdbcTemplate.queryForList(sql, trackingNumber);
    }

    public List<Map<String, Object>> findStops(
            String trackingNumber) {

        String sql =
                "SELECT RS.STOP_ID, RS.ROUTE_ID, RS.STOP_ORDER, " +
                "RS.LOCATION_NAME, RS.LATITUDE, RS.LONGITUDE, " +
                "RS.ARRIVAL_TIME " +
                "FROM ROUTE_STOPS RS " +
                "JOIN ROUTES R ON RS.ROUTE_ID = R.ROUTE_ID " +
                "JOIN ASSIGNMENTS A " +
                "ON R.ASSIGNMENT_ID = A.ASSIGNMENT_ID " +
                "JOIN SHIPMENTS S " +
                "ON A.SHIPMENT_ID = S.SHIPMENT_ID " +
                "WHERE S.TRACKING_NUMBER = ? " +
                "ORDER BY RS.STOP_ORDER";

        return jdbcTemplate.queryForList(sql, trackingNumber);
    }
}
