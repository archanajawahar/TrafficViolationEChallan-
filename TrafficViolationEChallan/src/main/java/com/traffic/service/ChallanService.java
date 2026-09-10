package com.traffic.service;

import com.traffic.exception.DuplicateChallanException;
import com.traffic.exception.InvalidViolationException;
import com.traffic.model.Challan;
import com.traffic.model.PaymentStatus;
import com.traffic.model.Vehicle;
import com.traffic.model.Violation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChallanService {

    private final List<Challan> challans =
            new ArrayList<>();

    public Challan generateChallan(
            Violation violation) {

        if (violation == null) {

            throw new InvalidViolationException(
                    "Violation cannot be null."
            );
        }

        for (Challan challan : challans) {

            if (challan.getViolation()
                    .getViolationId()
                    .equals(violation.getViolationId())) {

                throw new DuplicateChallanException(
                        "Challan already exists for violation: " +
                        violation.getViolationId()
                );
            }
        }

        String challanId =
                "CH-" +
                String.format(
                        "%03d",
                        challans.size() + 1
                );

        Challan challan =
                new Challan(
                        challanId,
                        violation
                );

        challans.add(challan);

        System.out.println(
                "E-Challan generated: " +
                challanId
        );

        return challan;
    }

    public void payChallan(
            String challanId) {

        Challan challan =
                findChallan(challanId);

        if (challan == null) {

            throw new InvalidViolationException(
                    "Challan not found: " +
                    challanId
            );
        }

        if (challan.getPaymentStatus()
                == PaymentStatus.PAID) {

            throw new InvalidViolationException(
                    "Challan is already paid."
            );
        }

        challan.markPaid();

        System.out.println(
                "Challan " +
                challanId +
                " paid successfully."
        );
    }

    public double calculateOutstandingFines(
            String vehicleNumber) {

        double total = 0;

        for (Challan challan : challans) {

            if (challan.getViolation()
                    .getVehicleNumber()
                    .equalsIgnoreCase(vehicleNumber)
                    &&
                    challan.getPaymentStatus()
                            == PaymentStatus.UNPAID) {

                total += challan.getViolation()
                        .getFineAmount();
            }
        }

        return total;
    }

    public String classifyVehicle(
            Vehicle vehicle) {

        int count =
                vehicle.getViolationHistory()
                        .size();

        if (count == 0) {

            return "LOW RISK";

        } else if (count <= 2) {

            return "MODERATE RISK";

        } else if (count <= 4) {

            return "HIGH RISK";

        } else {

            return "VERY HIGH RISK";
        }
    }

    private Challan findChallan(
            String challanId) {

        for (Challan challan : challans) {

            if (challan.getChallanId()
                    .equalsIgnoreCase(challanId)) {

                return challan;
            }
        }

        return null;
    }

    public List<Challan> getChallans() {

        return Collections.unmodifiableList(
                challans
        );
    }
}
