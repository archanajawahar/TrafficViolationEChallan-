package com.traffic;

import com.traffic.exception.DuplicateChallanException;
import com.traffic.exception.InvalidVehicleException;
import com.traffic.exception.InvalidViolationException;
import com.traffic.model.Challan;
import com.traffic.model.Vehicle;
import com.traffic.model.VehicleType;
import com.traffic.model.Violation;
import com.traffic.model.ViolationType;
import com.traffic.service.ChallanService;
import com.traffic.service.VehicleService;
import com.traffic.service.ViolationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TrafficViolationServiceTest {

    private VehicleService vehicleService;
    private ViolationService violationService;
    private ChallanService challanService;

    @BeforeEach
    void setUp() {

        vehicleService =
                new VehicleService();

        violationService =
                new ViolationService(
                        vehicleService
                );

        challanService =
                new ChallanService();
    }

    private Vehicle createVehicle() {

        Vehicle vehicle =
                new Vehicle(
                        "TN01AB1234",
                        "Arun Kumar",
                        "9876543210",
                        VehicleType.CAR
                );

        vehicleService.registerVehicle(
                vehicle
        );

        return vehicle;
    }

    private Violation createViolation(
            String id,
            ViolationType type,
            double speed,
            double permittedSpeed) {

        return new Violation(
                id,
                "TN01AB1234",
                type,
                "Vellore",
                LocalDateTime.now(),
                speed,
                permittedSpeed
        );
    }

    @Test
    void testRegisterVehicle() {

        Vehicle vehicle =
                createVehicle();

        assertNotNull(
                vehicleService.findVehicle(
                        vehicle.getVehicleNumber()
                )
        );
    }

    @Test
    void testOverSpeedingFineNormal() {

        createVehicle();

        Violation violation =
                createViolation(
                        "V001",
                        ViolationType.OVER_SPEEDING,
                        65,
                        60
                );

        violationService.detectViolation(
                violation
        );

        assertEquals(
                500,
                violation.getFineAmount()
        );
    }

    @Test
    void testOverSpeedingBoundaryAtTenKm() {

        createVehicle();

        Violation violation =
                createViolation(
                        "V002",
                        ViolationType.OVER_SPEEDING,
                        70,
                        60
                );

        violationService.detectViolation(
                violation
        );

        assertEquals(
                500,
                violation.getFineAmount()
        );
    }

    @Test
    void testOverSpeedingAboveTwentyKm() {

        createVehicle();

        Violation violation =
                createViolation(
                        "V003",
                        ViolationType.OVER_SPEEDING,
                        85,
                        60
                );

        violationService.detectViolation(
                violation
        );

        assertEquals(
                2000,
                violation.getFineAmount()
        );
    }

    @Test
    void testSignalViolationFine() {

        createVehicle();

        Violation violation =
                createViolation(
                        "V004",
                        ViolationType.SIGNAL_VIOLATION,
                        0,
                        0
                );

        violationService.detectViolation(
                violation
        );

        assertEquals(
                1500,
                violation.getFineAmount()
        );
    }

    @Test
    void testIllegalParkingFine() {

        createVehicle();

        Violation violation =
                createViolation(
                        "V005",
                        ViolationType.ILLEGAL_PARKING,
                        0,
                        0
                );

        violationService.detectViolation(
                violation
        );

        assertEquals(
                500,
                violation.getFineAmount()
        );
    }

    @Test
    void testRepeatedViolationHigherPenalty() {

        createVehicle();

        Violation first =
                createViolation(
                        "V006",
                        ViolationType.OVER_SPEEDING,
                        65,
                        60
                );

        violationService.detectViolation(
                first
        );

        Violation second =
                createViolation(
                        "V007",
                        ViolationType.OVER_SPEEDING,
                        65,
                        60
                );

        violationService.detectViolation(
                second
        );

        assertEquals(
                750,
                second.getFineAmount()
        );
    }

    @Test
    void testGenerateChallan() {

        createVehicle();

        Violation violation =
                createViolation(
                        "V008",
                        ViolationType.ILLEGAL_PARKING,
                        0,
                        0
                );

        violationService.detectViolation(
                violation
        );

        Challan challan =
                challanService.generateChallan(
                        violation
                );

        assertNotNull(challan);

        assertEquals(
                com.traffic.model.PaymentStatus.UNPAID,
                challan.getPaymentStatus()
        );
    }

    @Test
    void testDuplicateChallan() {

        createVehicle();

        Violation violation =
                createViolation(
                        "V009",
                        ViolationType.SIGNAL_VIOLATION,
                        0,
                        0
                );

        violationService.detectViolation(
                violation
        );

        challanService.generateChallan(
                violation
        );

        assertThrows(
                DuplicateChallanException.class,
                () ->
                        challanService.generateChallan(
                                violation
                        )
        );
    }

    @Test
    void testPaymentAndOutstandingFine() {

        createVehicle();

        Violation violation =
                createViolation(
                        "V010",
                        ViolationType.ILLEGAL_PARKING,
                        0,
                        0
                );

        violationService.detectViolation(
                violation
        );

        Challan challan =
                challanService.generateChallan(
                        violation
                );

        assertEquals(
                500,
                challanService
                        .calculateOutstandingFines(
                                "TN01AB1234"
                        )
        );

        challanService.payChallan(
                challan.getChallanId()
        );

        assertEquals(
                0,
                challanService
                        .calculateOutstandingFines(
                                "TN01AB1234"
                        )
        );
    }

    @Test
    void testInvalidVehicleInformation() {

        Vehicle invalidVehicle =
                new Vehicle(
                        "",
                        "",
                        "",
                        null
                );

        assertThrows(
                InvalidVehicleException.class,
                () ->
                        vehicleService.registerVehicle(
                                invalidVehicle
                        )
        );
    }

    @Test
    void testMultipleInvalidInputs() {

        createVehicle();

        Violation invalidViolation =
                new Violation(
                        "",
                        "",
                        null,
                        "",
                        null,
                        -10,
                        -20
                );

        assertThrows(
                InvalidViolationException.class,
                () ->
                        violationService.detectViolation(
                                invalidViolation
                        )
        );
    }
}
