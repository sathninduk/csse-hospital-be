// Patient.java
package com.csse.hospital.model.user;

import com.csse.hospital.model.Appointment;
import com.csse.hospital.model.HealthCard;
import com.csse.hospital.model.Payment;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Patient extends User {
    @OneToMany(mappedBy = "patient")
    private List<Payment> payments;

    @OneToOne(mappedBy = "patient")
    private HealthCard healthCard;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    // Attributes


    // Getters and Setters
}