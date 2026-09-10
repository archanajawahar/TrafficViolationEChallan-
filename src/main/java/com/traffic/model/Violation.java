package com.traffic.model;

import java.time.LocalDateTime;

public class Violation {

    private String violationId;
    private String vehicleNumber;
    private ViolationType violationType;
    private String location;
    private LocalDateTime timestamp;
    private double speed;
    private double permittedSpeed;
    private double fineAmount;

    public Violation(String violationId,
                     String vehicleNumber,
                     ViolationType violationType,
                     String location,
                     LocalDateTime timestamp,
                     double speed,
                     double permittedSpeed) {

        this.violationId = violationId;
        this.vehicleNumber = vehicleNumber;
        this.violationType = violationType;
        this.location = location;
        this.timestamp = timestamp;
        this.speed = speed;
        this.permittedSpeed = permittedSpeed;
    }

    public String getViolationId() {
        return violationId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public ViolationType getViolationType() {
        return violationType;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getSpeed() {
        return speed;
    }

    public double getPermittedSpeed() {
        return permittedSpeed;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    @Override
    public String toString() {

        return violationId +
                " | Vehicle: " + vehicleNumber +
                " | Type: " + violationType +
                " | Fine: ₹" + fineAmount;
    }
}
