
package com.logistics.fleet.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DRIVERS")
public class Driver {

    @Id
    @Column(name = "DRIVER_ID")
    private Long driverId;

    @Column(name = "FULL_NAME", nullable = false, length = 100)
    private String fullName;

    @Column(name = "PHONE", nullable = false, length = 20)
    private String phone;

    @Column(name = "LICENSE_NUMBER", nullable = false, length = 50)
    private String licenseNumber;

    @Column(name = "LICENSE_EXPIRY")
    private LocalDate licenseExpiry;

    @Column(name = "STATUS", length = 30)
    private String status;

    public Driver() {
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public LocalDate getLicenseExpiry() {
        return licenseExpiry;
    }

    public void setLicenseExpiry(LocalDate licenseExpiry) {
        this.licenseExpiry = licenseExpiry;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
