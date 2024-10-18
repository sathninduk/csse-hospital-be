package com.csse.hospital.model;

import com.csse.hospital.model.user.Doctor;
import com.csse.hospital.model.user.Patient;
import jakarta.persistence.*;

@Entity
public class MedicalReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany
    @JoinColumn(name="doctor_id")
    private Doctor doctor;

    private String prescription;

    @Lob
    private byte[] document;

    public MedicalReport() {
    }


    public MedicalReport(Patient patient, Doctor doctor, String prescription, byte[] document, Long pId,Long docId) {
        this.patient = patient;
        this.doctor = doctor;
        this.prescription = prescription;
        this.document = document;
        this.pId = pId;
        this.docId = docId;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    private Long pId;
    private Long docId;
}

