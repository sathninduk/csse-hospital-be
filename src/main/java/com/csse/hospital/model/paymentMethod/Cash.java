// Cash.java
package com.csse.hospital.model.paymentMethod;

import com.csse.hospital.model.paymentMethod.PaymentMethod;

import jakarta.persistence.*;

@Entity
public class Cash extends PaymentMethod {
    private String receiptNumber;

    // Getters and Setters
}