package com.traffic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vehicle {

    private String vehicleNumber;
    private String ownerName;
    private String ownerPhone;
    private VehicleType vehicleType;

    private final List<Violation> violationHistory =
            new ArrayList<>();

    public Vehicle(String vehicleNumber,
                   String ownerName,
                   String ownerPhone,
                   VehicleType vehicleType) {

        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void addViolation(Violation violation) {
        violationHistory.add(violation);
    }

    public List<Violation> getViolationHistory() {
        return Collections.unmodifiableList(violationHistory);
    }

    @Override
    public String toString() {

        return vehicleNumber +
                " | Owner: " + ownerName +
                " | Phone: " + ownerPhone +
                " | Type: " + vehicleType +
                " | Violations: " + violationHistory.size();
    }
}
