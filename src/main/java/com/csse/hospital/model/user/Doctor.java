// Doctor.java
package com.csse.hospital.model.user;

import com.csse.hospital.model.Appointment;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Doctor extends User {
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

    // Attributes

    // Getters and Setters

}