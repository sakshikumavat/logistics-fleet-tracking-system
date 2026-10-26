
package com.logistics.fleet.service;

import com.logistics.fleet.entity.AuditLog;
import com.logistics.fleet.repository.AuditLogRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository repository;
    private final JdbcTemplate jdbcTemplate;

    public AuditLogService(
            AuditLogRepository repository,
            JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public AuditLog logAction(
            Long userId,
            String action,
            String entityName,
            Long entityId,
            String details) {

        Long nextId = jdbcTemplate.queryForObject(
                "SELECT NVL(MAX(AUDIT_ID), 0) + 1 FROM AUDIT_LOGS",
                Long.class);

        AuditLog log = new AuditLog();
        log.setAuditId(nextId);
        log.setUserId(userId);
        log.setAction(action);
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setDetails(details);
        log.setCreatedAt(LocalDateTime.now());

        return repository.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return repository.findAll();
    }
}
