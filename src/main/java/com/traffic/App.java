package com.traffic;

import com.traffic.model.Challan;
import com.traffic.model.Vehicle;
import com.traffic.model.VehicleType;
import com.traffic.model.Violation;
import com.traffic.model.ViolationType;
import com.traffic.service.ChallanService;
import com.traffic.service.VehicleService;
import com.traffic.service.ViolationService;

import java.time.LocalDateTime;

public class App {

    public static void main(String[] args) {

        VehicleService vehicleService =
                new VehicleService();

        ViolationService violationService =
                new ViolationService(
                        vehicleService
                );

        ChallanService challanService =
                new ChallanService();

        System.out.println(
                "===== TRAFFIC VIOLATION AND E-CHALLAN SYSTEM ====="
        );

        Vehicle v1 =
                new Vehicle(
                        "TN01AB1234",
                        "Arun Kumar",
                        "9876543210",
                        VehicleType.CAR
                );

        Vehicle v2 =
                new Vehicle(
                        "TN02CD5678",
                        "Rahul",
                        "9876543211",
                        VehicleType.TWO_WHEELER
                );

        Vehicle v3 =
                new Vehicle(
                        "TN03EF9012",
                        "Kumar",
                        "9876543212",
                        VehicleType.BUS
                );

        vehicleService.registerVehicle(v1);
        vehicleService.registerVehicle(v2);
        vehicleService.registerVehicle(v3);

        System.out.println(
                "\n===== VIOLATIONS ====="
        );

        Violation violation1 =
                new Violation(
                        "V001",
                        "TN01AB1234",
                        ViolationType.OVER_SPEEDING,
                        "Vellore Main Road",
                        LocalDateTime.now(),
                        75,
                        60
                );

        violationService.detectViolation(
                violation1
        );

        Challan challan1 =
                challanService.generateChallan(
                        violation1
                );

        Violation violation2 =
                new Violation(
                        "V002",
                        "TN02CD5678",
                        ViolationType.SIGNAL_VIOLATION,
                        "Katpadi Junction",
                        LocalDateTime.now(),
                        0,
                        0
                );

        violationService.detectViolation(
                violation2
        );

        Challan challan2 =
                challanService.generateChallan(
                        violation2
                );

        Violation violation3 =
                new Violation(
                        "V003",
                        "TN01AB1234",
                        ViolationType.OVER_SPEEDING,
                        "Chennai Highway",
                        LocalDateTime.now(),
                        85,
                        60
                );

        violationService.detectViolation(
                violation3
        );

        Challan challan3 =
                challanService.generateChallan(
                        violation3
                );

        System.out.println(
                "\n===== CHALLAN DETAILS ====="
        );

        for (Challan challan :
                challanService.getChallans()) {

            System.out.println(challan);
        }

        System.out.println(
                "\n===== PAYMENT ====="
        );

        challanService.payChallan(
                challan1.getChallanId()
        );

        System.out.println(
                "\n===== OUTSTANDING FINES ====="
        );

        System.out.printf(
                "TN01AB1234 Outstanding: ₹%.2f%n",
                challanService
                        .calculateOutstandingFines(
                                "TN01AB1234"
                        )
        );

        System.out.printf(
                "TN02CD5678 Outstanding: ₹%.2f%n",
                challanService
                        .calculateOutstandingFines(
                                "TN02CD5678"
                        )
        );

        System.out.println(
                "\n===== VEHICLE CLASSIFICATION ====="
        );

        System.out.println(
                "TN01AB1234: " +
                challanService.classifyVehicle(v1)
        );

        System.out.println(
                "TN02CD5678: " +
                challanService.classifyVehicle(v2)
        );

        System.out.println(
                "TN03EF9012: " +
                challanService.classifyVehicle(v3)
        );

        System.out.println(
                "\n===== SYSTEM COMPLETED ====="
        );
    }
}
