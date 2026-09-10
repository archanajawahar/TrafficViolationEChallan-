package com.traffic.model;

import java.time.LocalDateTime;

public class Challan {

    private String challanId;
    private Violation violation;
    private PaymentStatus paymentStatus;
    private LocalDateTime generatedAt;

    public Challan(String challanId, Violation violation) {

        this.challanId = challanId;
        this.violation = violation;
        this.paymentStatus = PaymentStatus.UNPAID;
        this.generatedAt = LocalDateTime.now();
    }

    public String getChallanId() {
        return challanId;
    }

    public Violation getViolation() {
        return violation;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void markPaid() {
        paymentStatus = PaymentStatus.PAID;
    }

    @Override
    public String toString() {

        return "Challan ID: " + challanId +
                " | Vehicle: " +
                violation.getVehicleNumber() +
                " | Violation: " +
                violation.getViolationType() +
                " | Fine: ₹" +
                violation.getFineAmount() +
                " | Payment: " +
                paymentStatus;
    }
}
