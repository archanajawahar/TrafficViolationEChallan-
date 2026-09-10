package com.traffic.service;

import com.traffic.exception.InvalidViolationException;
import com.traffic.model.Vehicle;
import com.traffic.model.Violation;
import com.traffic.model.ViolationType;

public class ViolationService {

    private final VehicleService vehicleService;

    public ViolationService(
            VehicleService vehicleService) {

        this.vehicleService = vehicleService;
    }

    public void detectViolation(
            Violation violation) {

        validateViolation(violation);

        Vehicle vehicle =
                vehicleService.findVehicle(
                        violation.getVehicleNumber()
                );

        if (vehicle == null) {

            throw new InvalidViolationException(
                    "Vehicle is not registered: " +
                    violation.getVehicleNumber()
            );
        }

        long previousViolations =
                vehicle.getViolationHistory()
                        .stream()
                        .filter(v ->
                                v.getViolationType()
                                        == violation
                                        .getViolationType())
                        .count();

        double fine =
                calculateFine(
                        violation,
                        (int) previousViolations
                );

        violation.setFineAmount(fine);

        vehicle.addViolation(violation);

        System.out.println(
                "Violation detected: " +
                violation.getViolationType()
        );

        System.out.printf(
                "Fine amount: ₹%.2f%n",
                fine
        );
    }

    public double calculateFine(
            Violation violation,
            int previousViolations) {

        double baseFine;

        switch (violation.getViolationType()) {

            case OVER_SPEEDING:

                if (violation.getSpeed() <=
                        violation.getPermittedSpeed()) {

                    throw new InvalidViolationException(
                            "Speed must exceed permitted speed."
                    );
                }

                double excess =
                        violation.getSpeed()
                                - violation.getPermittedSpeed();

                if (excess <= 10) {

                    baseFine = 500;

                } else if (excess <= 20) {

                    baseFine = 1000;

                } else {

                    baseFine = 2000;
                }

                break;

            case SIGNAL_VIOLATION:

                baseFine = 1500;
                break;

            case ILLEGAL_PARKING:

                baseFine = 500;
                break;

            default:

                throw new InvalidViolationException(
                        "Unknown violation type."
                );
        }

        if (previousViolations > 0) {

            baseFine =
                    baseFine *
                    (1 + previousViolations * 0.5);
        }

        return baseFine;
    }

    private void validateViolation(
            Violation violation) {

        if (violation == null) {

            throw new InvalidViolationException(
                    "Violation cannot be null."
            );
        }

        if (violation.getViolationId() == null ||
                violation.getViolationId().isBlank()) {

            throw new InvalidViolationException(
                    "Violation ID is required."
            );
        }

        if (violation.getVehicleNumber() == null ||
                violation.getVehicleNumber().isBlank()) {

            throw new InvalidViolationException(
                    "Vehicle number is required."
            );
        }

        if (violation.getViolationType() == null) {

            throw new InvalidViolationException(
                    "Violation type is required."
            );
        }

        if (violation.getLocation() == null ||
                violation.getLocation().isBlank()) {

            throw new InvalidViolationException(
                    "Violation location is required."
            );
        }

        if (violation.getTimestamp() == null) {

            throw new InvalidViolationException(
                    "Violation timestamp is required."
            );
        }

        if (violation.getSpeed() < 0 ||
                violation.getPermittedSpeed() < 0) {

            throw new InvalidViolationException(
                    "Speed cannot be negative."
            );
        }
    }
}
