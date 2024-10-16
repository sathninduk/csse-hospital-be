// HealthCard.java
package com.csse.hospital.model;

import com.csse.hospital.model.user.Patient;
import jakarta.persistence.*;

@Entity
public class HealthCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Getters and Setters
}