package com.logistics.fleet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "FleetTrack Logistics OS Backend");
        health.put("version", "2.4.0");
        health.put("timestamp", LocalDateTime.now());

        try {
            jdbcTemplate.execute("SELECT 1 FROM DUAL");
            health.put("database", "CONNECTED");
            health.put("databaseType", "Oracle Database 11g Express Edition");
        } catch (Exception e) {
            health.put("database", "DEGRADED");
            health.put("databaseError", e.getMessage());
        }

        return ResponseEntity.ok(health);
    }
}
