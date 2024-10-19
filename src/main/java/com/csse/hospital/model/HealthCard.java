// HealthCard.java
package com.csse.hospital.model;

import com.csse.hospital.model.user.Patient;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class HealthCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int status;

    @Column(name = "request_date", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp requestDate;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}