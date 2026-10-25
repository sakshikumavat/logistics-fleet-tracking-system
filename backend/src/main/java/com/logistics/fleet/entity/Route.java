
package com.logistics.fleet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ROUTES")
public class Route {

    @Id
    @Column(name = "ROUTE_ID")
    private Long routeId;

    @Column(name = "ASSIGNMENT_ID", nullable = false)
    private Long assignmentId;

    @Column(name = "ROUTE_NAME", length = 100)
    private String routeName;

    @Column(name = "START_LOCATION", length = 200)
    private String startLocation;

    @Column(name = "END_LOCATION", length = 200)
    private String endLocation;

    @Column(name = "DISTANCE_KM", precision = 10, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "ESTIMATED_MINUTES")
    private Integer estimatedMinutes;

    @Column(name = "STATUS", length = 30)
    private String status;

    // Default constructor
    public Route() {
    }

    // Getters and Setters
    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(Integer estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
