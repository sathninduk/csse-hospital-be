package com.csse.hospital.service;

import com.csse.hospital.model.Payment;

public interface PaymentUpdateStrategy {
    Payment updatePayment(Payment existingPayment, Payment newPayment);
}