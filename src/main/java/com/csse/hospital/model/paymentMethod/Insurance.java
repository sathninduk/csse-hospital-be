// Insurance.java
package com.csse.hospital.model.paymentMethod;

import com.csse.hospital.model.paymentMethod.PaymentMethod;

import jakarta.persistence.*;

@Entity
public class Insurance extends PaymentMethod {
    private String policyNumber;
    private String provider;

    // Getters and Setters
}