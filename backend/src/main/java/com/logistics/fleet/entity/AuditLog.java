
package com.logistics.fleet.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AUDIT_LOGS")
public class AuditLog {

    @Id
    @Column(name = "AUDIT_ID")
    private Long auditId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "ENTITY_NAME")
    private String entityName;

    @Column(name = "ENTITY_ID")
    private Long entityId;

    @Column(name = "DETAILS")
    private String details;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
