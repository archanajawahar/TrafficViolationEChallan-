package com.traffic.service;

import com.traffic.exception.InvalidVehicleException;
import com.traffic.model.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VehicleService {

    private final List<Vehicle> vehicles =
            new ArrayList<>();

    public void registerVehicle(Vehicle vehicle) {

        validateVehicle(vehicle);

        if (findVehicle(
                vehicle.getVehicleNumber()) != null) {

            throw new InvalidVehicleException(
                    "Vehicle already registered: " +
                    vehicle.getVehicleNumber()
            );
        }

        vehicles.add(vehicle);

        System.out.println(
                "Vehicle registered: " +
                vehicle.getVehicleNumber()
        );
    }

    public Vehicle findVehicle(
            String vehicleNumber) {

        for (Vehicle vehicle : vehicles) {

            if (vehicle.getVehicleNumber()
                    .equalsIgnoreCase(vehicleNumber)) {

                return vehicle;
            }
        }

        return null;
    }

    private void validateVehicle(
            Vehicle vehicle) {

        if (vehicle == null) {

            throw new InvalidVehicleException(
                    "Vehicle cannot be null."
            );
        }

        if (vehicle.getVehicleNumber() == null ||
                vehicle.getVehicleNumber().isBlank()) {

            throw new InvalidVehicleException(
                    "Vehicle number is required."
            );
        }

        if (vehicle.getOwnerName() == null ||
                vehicle.getOwnerName().isBlank()) {

            throw new InvalidVehicleException(
                    "Owner name is required."
            );
        }

        if (vehicle.getOwnerPhone() == null ||
                vehicle.getOwnerPhone().isBlank()) {

            throw new InvalidVehicleException(
                    "Owner phone is required."
            );
        }

        if (vehicle.getVehicleType() == null) {

            throw new InvalidVehicleException(
                    "Vehicle type is required."
            );
        }
    }

    public List<Vehicle> getVehicles() {

        return Collections.unmodifiableList(
                vehicles
        );
    }
}
