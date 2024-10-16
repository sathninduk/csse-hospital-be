// CreditCard.java
package com.csse.hospital.model.paymentMethod;

import com.csse.hospital.model.paymentMethod.PaymentMethod;

import jakarta.persistence.*;

@Entity
public class CreditCard extends PaymentMethod {
    private String cardNumber;
    private String cardHolderName;

    // Getters and Setters
}