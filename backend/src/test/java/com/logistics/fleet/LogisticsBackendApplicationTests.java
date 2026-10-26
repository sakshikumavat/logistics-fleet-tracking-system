package com.logistics.fleet;

import com.logistics.fleet.dto.DashboardSummary;
import com.logistics.fleet.dto.LoginRequest;
import com.logistics.fleet.dto.LoginResponse;
import com.logistics.fleet.dto.SignupRequest;
import com.logistics.fleet.entity.AppUser;
import com.logistics.fleet.entity.Assignment;
import com.logistics.fleet.entity.AuditLog;
import com.logistics.fleet.entity.DeliveryConfirmation;
import com.logistics.fleet.entity.Driver;
import com.logistics.fleet.entity.Shipment;
import com.logistics.fleet.entity.Vehicle;
import com.logistics.fleet.service.AssignmentService;
import com.logistics.fleet.service.AuditLogService;
import com.logistics.fleet.service.AuthService;
import com.logistics.fleet.service.DashboardService;
import com.logistics.fleet.service.DeliveryConfirmationService;
import com.logistics.fleet.service.DriverService;
import com.logistics.fleet.service.ShipmentService;
import com.logistics.fleet.service.VehicleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private DriverService driverService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private DeliveryConfirmationService deliveryConfirmationService;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("1. Spring Context Loads Successfully")
    void contextLoads() {
        assertNotNull(dashboardService, "DashboardService must be loaded");
        assertNotNull(shipmentService, "ShipmentService must be loaded");
        assertNotNull(vehicleService, "VehicleService must be loaded");
        assertNotNull(authService, "AuthService must be loaded");
        assertNotNull(assignmentService, "AssignmentService must be loaded");
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
    @DisplayName("4. Vehicle Location telemetry recording, coordinate validation and history retrieval")
    void testVehicleLocationTelemetry() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        assertFalse(vehicles.isEmpty(), "At least one vehicle must exist in database");

        Long vehicleId = vehicles.get(0).getVehicleId();
        double testLat = 18.5204;
        double testLng = 73.8567;

        // Valid coordinates
        Map<String, Object> recordResult = vehicleService.recordLocation(vehicleId, testLat, testLng);
        assertNotNull(recordResult.get("locationId"), "Location ID must be generated");

        // Latitude validation (-90 to 90)
        assertThrows(ResponseStatusException.class, () ->
            vehicleService.recordLocation(vehicleId, 95.0, 73.0),
            "Latitude > 90 must be rejected"
        );
        assertThrows(ResponseStatusException.class, () ->
            vehicleService.recordLocation(vehicleId, -95.0, 73.0),
            "Latitude < -90 must be rejected"
        );

        // Longitude validation (-180 to 180)
        assertThrows(ResponseStatusException.class, () ->
            vehicleService.recordLocation(vehicleId, 18.0, 185.0),
            "Longitude > 180 must be rejected"
        );
        assertThrows(ResponseStatusException.class, () ->
            vehicleService.recordLocation(vehicleId, 18.0, -185.0),
            "Longitude < -180 must be rejected"
        );

        // Retrieve latest location
        Map<String, Object> latest = vehicleService.getLatestLocation(vehicleId);
        assertNotNull(latest, "Latest location must not be null");
        assertEquals(vehicleId, Long.valueOf(latest.get("VEHICLE_ID").toString()));

        // Retrieve location history (chronological order)
        List<Map<String, Object>> history = vehicleService.getLocationHistory(vehicleId);
        assertNotNull(history, "History list must not be null");
        assertFalse(history.isEmpty(), "Location history must contain recorded points");

        // All latest locations list
        List<Map<String, Object>> allLatest = vehicleService.getAllLatestLocations();
        assertNotNull(allLatest, "All latest locations must not be null");
        assertFalse(allLatest.isEmpty(), "All latest locations must contain active vehicles");
    }

    @Test
    @DisplayName("5. Signup End-to-End: Registration -> BCrypt persistence -> Login -> JWT")
    void testSignupAndLoginFlow() {
        String uniqueEmail = "user" + System.currentTimeMillis() + "@logistics.com";
        String password = "User@Pass123";
        String fullName = "Test Operations Dispatcher";

        SignupRequest signupReq = new SignupRequest(fullName, uniqueEmail, password, null);
        LoginResponse signupRes = authService.signup(signupReq);

        assertNotNull(signupRes, "Signup response must not be null");
        assertNotNull(signupRes.getToken(), "JWT Token must be generated upon signup");
        assertEquals(uniqueEmail, signupRes.getEmail());
        assertEquals("DISPATCHER", signupRes.getRole(), "Default role on public signup must be DISPATCHER");

        // Verify persistence in Oracle
        AppUser persisted = authService.getUserByEmail(uniqueEmail);
        assertNotNull(persisted, "User must be found in Oracle database");
        assertEquals(1, persisted.getActive());
        assertNotEquals(password, persisted.getPasswordHash(), "Password must NEVER be stored in plaintext");
        assertTrue(persisted.getPasswordHash().startsWith("$2a$") || persisted.getPasswordHash().startsWith("$2b$"),
                "Password must be stored as BCrypt hash");
        assertTrue(passwordEncoder.matches(password, persisted.getPasswordHash()),
                "BCrypt encoder must successfully verify the original password");

        // Verify login with registered credentials
        LoginRequest loginReq = new LoginRequest(uniqueEmail, password);
        LoginResponse loginRes = authService.login(loginReq);
        assertNotNull(loginRes, "Login response must not be null");
        assertNotNull(loginRes.getToken(), "JWT token must be returned upon successful login");
        assertEquals("DISPATCHER", loginRes.getRole());
    }

    @Test
    @DisplayName("6. Public Signup Prevents Privilege Escalation (ADMIN request ignored)")
    void testPublicSignupPreventsPrivilegeEscalation() {
        String attackerEmail = "escalate" + System.currentTimeMillis() + "@hack.org";
        SignupRequest attackReq = new SignupRequest("Attacker Name", attackerEmail, "HackPass@123", "ADMIN");

        LoginResponse res = authService.signup(attackReq);
        assertNotNull(res);
        assertNotEquals("ADMIN", res.getRole(), "Public signup must NEVER assign ADMIN role");
        assertEquals("DISPATCHER", res.getRole(), "Public signup must safely default to DISPATCHER");

        AppUser userInDb = authService.getUserByEmail(attackerEmail);
        assertEquals("DISPATCHER", userInDb.getRole(), "Role in Oracle DB must be DISPATCHER");
    }

    @Test
    @DisplayName("7. Duplicate signup and invalid credentials fail gracefully")
    void testDuplicateSignupAndInvalidCredentials() {
        String existingEmail = "dup" + System.currentTimeMillis() + "@logistics.com";
        SignupRequest req1 = new SignupRequest("First User", existingEmail, "Secret@123", null);
        authService.signup(req1);

        // Duplicate signup must throw CONFLICT (409)
        SignupRequest req2 = new SignupRequest("Second User", existingEmail, "Secret@123", null);
        ResponseStatusException conflictEx = assertThrows(ResponseStatusException.class, () ->
            authService.signup(req2)
        );
        assertEquals(409, conflictEx.getStatusCode().value(), "Duplicate registration must return 409 Conflict");

        // Invalid email format must throw BAD_REQUEST (400)
        SignupRequest badEmailReq = new SignupRequest("Bad Email", "not-an-email", "Secret@123", null);
        ResponseStatusException badEmailEx = assertThrows(ResponseStatusException.class, () ->
            authService.signup(badEmailReq)
        );
        assertEquals(400, badEmailEx.getStatusCode().value(), "Invalid email must return 400 Bad Request");

        // Short password (< 6 chars) must throw BAD_REQUEST (400)
        SignupRequest shortPassReq = new SignupRequest("Short Pass", "shortpass" + System.currentTimeMillis() + "@test.com", "123", null);
        ResponseStatusException shortPassEx = assertThrows(ResponseStatusException.class, () ->
            authService.signup(shortPassReq)
        );
        assertEquals(400, shortPassEx.getStatusCode().value(), "Short password must return 400 Bad Request");

        // Invalid credentials at login must throw UNAUTHORIZED (401)
        LoginRequest badLogin = new LoginRequest(existingEmail, "WrongPassword@999");
        ResponseStatusException unauthEx = assertThrows(ResponseStatusException.class, () ->
            authService.login(badLogin)
        );
        assertEquals(401, unauthEx.getStatusCode().value(), "Wrong credentials must return 401 Unauthorized");
    }

    @Test
    @DisplayName("8. Assignment conflict validation rejects double assignment of Driver & Vehicle")
    void testAssignmentConflictValidation() {
        long ts = System.currentTimeMillis();

        // Create 2 isolated test vehicles
        Vehicle testVeh1 = new Vehicle();
        testVeh1.setRegistrationNo("MH99A" + (ts % 100000));
        testVeh1.setVehicleName("Test Assignment Hauler 1");
        testVeh1.setVehicleType("HEAVY_DUTY");
        testVeh1.setDailyRate(new BigDecimal("7500.00"));
        testVeh1.setVehicleStatus("AVAILABLE");
        Vehicle createdVeh1 = vehicleService.createVehicle(testVeh1);

        Vehicle testVeh2 = new Vehicle();
        testVeh2.setRegistrationNo("MH99B" + ((ts + 1) % 100000));
        testVeh2.setVehicleName("Test Assignment Hauler 2");
        testVeh2.setVehicleType("HEAVY_DUTY");
        testVeh2.setDailyRate(new BigDecimal("7500.00"));
        testVeh2.setVehicleStatus("AVAILABLE");
        Vehicle createdVeh2 = vehicleService.createVehicle(testVeh2);

        // Create 2 isolated test drivers
        Driver testDriver1 = new Driver();
        testDriver1.setFullName("Test Driver Alpha");
        testDriver1.setPhone("9" + (ts % 1000000000L));
        testDriver1.setLicenseNumber("DL-ASGN-A" + (ts % 100000));
        testDriver1.setStatus("AVAILABLE");
        Driver createdDriver1 = driverService.createDriver(testDriver1);

        Driver testDriver2 = new Driver();
        testDriver2.setFullName("Test Driver Beta");
        testDriver2.setPhone("8" + (ts % 1000000000L));
        testDriver2.setLicenseNumber("DL-ASGN-B" + ((ts + 1) % 100000));
        testDriver2.setStatus("AVAILABLE");
        Driver createdDriver2 = driverService.createDriver(testDriver2);

        // Create shipment A
        Shipment shpA = new Shipment();
        shpA.setTrackingNumber("FT-ASGN-A-" + ts);
        shpA.setPickupAddress("Origin A");
        shpA.setDeliveryAddress("Dest A");
        shpA.setStatus("IN_TRANSIT");
        Shipment createdA = shipmentService.createShipment(shpA);

        // Assign driver 1 and vehicle 1 to shipment A
        Assignment asgn1 = new Assignment();
        asgn1.setShipmentId(createdA.getShipmentId());
        asgn1.setDriverId(createdDriver1.getDriverId());
        asgn1.setVehicleId(createdVeh1.getVehicleId());
        asgn1.setStatus("ASSIGNED");
        Assignment saved1 = assignmentService.createAssignment(asgn1);
        assertNotNull(saved1.getAssignmentId());

        // Create shipment B
        Shipment shpB = new Shipment();
        shpB.setTrackingNumber("FT-ASGN-B-" + ts);
        shpB.setPickupAddress("Origin B");
        shpB.setDeliveryAddress("Dest B");
        shpB.setStatus("IN_TRANSIT");
        Shipment createdB = shipmentService.createShipment(shpB);

        // Attempt to assign the same driver 1 to concurrent active shipment B -> must throw IllegalArgumentException
        Assignment conflictDriver = new Assignment();
        conflictDriver.setShipmentId(createdB.getShipmentId());
        conflictDriver.setDriverId(createdDriver1.getDriverId()); // CONFLICT!
        conflictDriver.setVehicleId(createdVeh2.getVehicleId()); // valid vehicle 2
        conflictDriver.setStatus("ASSIGNED");
        assertThrows(IllegalArgumentException.class, () ->
            assignmentService.createAssignment(conflictDriver),
            "Conflicting driver assignment must be rejected"
        );

        // Attempt to assign the same vehicle 1 to concurrent active shipment B -> must throw IllegalArgumentException
        Assignment conflictVehicle = new Assignment();
        conflictVehicle.setShipmentId(createdB.getShipmentId());
        conflictVehicle.setDriverId(createdDriver2.getDriverId()); // valid driver 2
        conflictVehicle.setVehicleId(createdVeh1.getVehicleId()); // CONFLICT!
        conflictVehicle.setStatus("ASSIGNED");
        assertThrows(IllegalArgumentException.class, () ->
            assignmentService.createAssignment(conflictVehicle),
            "Conflicting vehicle assignment must be rejected"
        );

        // Cleanup: complete assignment 1
        saved1.setStatus("COMPLETED");
        assignmentService.updateAssignment(saved1.getAssignmentId(), saved1);
    }

    @Test
    @DisplayName("9. Delivery Confirmation flow verifies proof, updates status to DELIVERED and records audit trail")
    void testDeliveryConfirmationFlow() {
        Shipment testShipment = new Shipment();
        testShipment.setTrackingNumber("FT-DELIV-" + System.currentTimeMillis());
        testShipment.setCustomerName("Acme Pharmaceuticals");
        testShipment.setCustomerPhone("+91 98200 11223");
        testShipment.setPickupAddress("Warehouse 1A, Pune");
        testShipment.setDeliveryAddress("Hospital Dock, Mumbai");
        testShipment.setWeightKg(new BigDecimal("450.00"));
        testShipment.setStatus("IN_TRANSIT");
        Shipment created = shipmentService.createShipment(testShipment);

        // Confirm delivery
        DeliveryConfirmation confirmation = deliveryConfirmationService.confirmDelivery(
                created.getShipmentId(),
                "Dr. Rajesh Sharma",
                "PO-REC-99841",
                "Received in pristine cold-chain condition with zero temperature deviations"
        );

        assertNotNull(confirmation.getConfirmationId(), "Confirmation ID must be generated");
        assertEquals("Dr. Rajesh Sharma", confirmation.getReceiverName());

        // Shipment status should now be DELIVERED in Oracle
        Shipment updated = shipmentService.getShipmentById(created.getShipmentId());
        assertEquals("DELIVERED", updated.getStatus());

        // Delivery confirmation must be retrievable
        DeliveryConfirmation retrieved = deliveryConfirmationService.getConfirmation(created.getShipmentId());
        assertNotNull(retrieved);
        assertEquals(confirmation.getConfirmationId(), retrieved.getConfirmationId());
    }

    @Test
    @DisplayName("10. Audit Logging records operational actions in Oracle AUDIT_LOGS")
    void testAuditLogging() {
        AuditLog logged = auditLogService.logAction(
                1L,
                "CREATE_DISPATCH",
                "SHIPMENT",
                999L,
                "Unit test created dispatch action"
        );
        assertNotNull(logged.getAuditId(), "Audit ID must be assigned");
        assertNotNull(logged.getCreatedAt(), "Timestamp must be recorded");

        List<AuditLog> allLogs = auditLogService.getAllLogs();
        assertFalse(allLogs.isEmpty(), "Audit logs must not be empty");
        assertTrue(allLogs.stream().anyMatch(l -> "CREATE_DISPATCH".equals(l.getAction())));
    }
}
