package com.csse.hospital.service;

import com.csse.hospital.model.Payment;

public class DefaultPaymentUpdateStrategy implements PaymentUpdateStrategy {
    @Override
    public Payment updatePayment(Payment existingPayment, Payment newPayment) {
        if (newPayment.getAmount() != 0) {
            existingPayment.setAmount(newPayment.getAmount());
        }
        if (newPayment.getPatient() != null) {
            existingPayment.setPatient(newPayment.getPatient());
        }
        if (newPayment.getPaymentMethod() != null) {
            existingPayment.setPaymentMethod(newPayment.getPaymentMethod());
        }
        if (newPayment.getStatus() != 0) {
            existingPayment.setStatus(newPayment.getStatus());
        }
        return existingPayment;
    }
}