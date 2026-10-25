package com.logistics.fleet;

import com.logistics.fleet.dto.DashboardSummary;
import com.logistics.fleet.entity.Shipment;
import com.logistics.fleet.entity.Vehicle;
import com.logistics.fleet.service.DashboardService;
import com.logistics.fleet.service.ShipmentService;
import com.logistics.fleet.service.VehicleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LogisticsBackendApplicationTests {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private VehicleService vehicleService;

    @Test
    @DisplayName("1. Spring Context Loads Successfully")
    void contextLoads() {
        assertNotNull(dashboardService, "DashboardService must be loaded");
        assertNotNull(shipmentService, "ShipmentService must be loaded");
        assertNotNull(vehicleService, "VehicleService must be loaded");
    }

    @Test
    @DisplayName("2. Dashboard Summary returns real metrics from Oracle Database")
    void testDashboardSummaryMetrics() {
        DashboardSummary summary = dashboardService.getSummary();
        assertNotNull(summary, "Dashboard summary must not be null");
        assertTrue(summary.getTotalVehicles() >= 0, "Total vehicles must be non-negative");
        assertTrue(summary.getTotalShipments() >= 0, "Total shipments must be non-negative");
        assertTrue(summary.getAvailableVehicles() >= 0, "Available vehicles must be non-negative");
        assertTrue(summary.getActiveTrips() >= 0, "Active trips must be non-negative");
        assertNotNull(summary.getTotalFuelExpense(), "Fuel expense must not be null");
        assertNotNull(summary.getTotalMaintenanceExpense(), "Maintenance expense must not be null");
    }

    @Test
    @DisplayName("3. Create Shipment persists into Oracle Database and updates count")
    void testCreateShipmentPersistence() {
        long countBefore = dashboardService.getSummary().getTotalShipments();

        Shipment testShipment = new Shipment();
        testShipment.setTrackingNumber("FT-UNIT-" + System.currentTimeMillis());
        testShipment.setCustomerName("Unit Test Customer Corp");
        testShipment.setCustomerPhone("+91 99999 88888");
        testShipment.setPickupAddress("Pune Test Depot Terminal");
        testShipment.setDeliveryAddress("Mumbai Test Dock Gate 4");
        testShipment.setWeightKg(new BigDecimal("1500.50"));
        testShipment.setStatus("IN_TRANSIT");
        testShipment.setExpectedDelivery(LocalDate.now().plusDays(1));

        Shipment created = shipmentService.createShipment(testShipment);
        assertNotNull(created.getShipmentId(), "Created shipment ID must be assigned");
        assertEquals("IN_TRANSIT", created.getStatus());

        long countAfter = dashboardService.getSummary().getTotalShipments();
        assertEquals(countBefore + 1, countAfter, "Total shipments count must increment by exactly 1 in Oracle");

        // Verify status update and history recording
        Shipment statusUpdated = shipmentService.updateShipmentStatus(created.getShipmentId(), "DELIVERED", "Delivered via unit test");
        assertEquals("DELIVERED", statusUpdated.getStatus());

        List<Map<String, Object>> history = shipmentService.getShipmentHistory(created.getShipmentId());
        assertFalse(history.isEmpty(), "Shipment status history must record transition");
        assertEquals("DELIVERED", history.get(0).get("NEW_STATUS"));
    }

    @Test
    @DisplayName("4. Vehicle Location telemetry recording and retrieval")
    void testVehicleLocationTelemetry() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        assertFalse(vehicles.isEmpty(), "At least one vehicle must exist in database");

        Long vehicleId = vehicles.get(0).getVehicleId();
        double testLat = 18.5204;
        double testLng = 73.8567;

        Map<String, Object> recordResult = vehicleService.recordLocation(vehicleId, testLat, testLng);
        assertNotNull(recordResult.get("locationId"), "Location ID must be generated");

        Map<String, Object> latest = vehicleService.getLatestLocation(vehicleId);
        assertNotNull(latest, "Latest location must not be null");
        assertEquals(vehicleId, Long.valueOf(latest.get("VEHICLE_ID").toString()));
    }
}
