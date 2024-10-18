package com.csse.hospital.controller;

import com.csse.hospital.model.MedicalReport;
import com.csse.hospital.model.user.Doctor;
import com.csse.hospital.model.user.Patient;
import com.csse.hospital.repository.DoctorRepository;
import com.csse.hospital.repository.MedicalReportRepository;
import com.csse.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicalreport")
public class MedicalReportController {

    @Autowired
    MedicalReportRepository medicalReportRepository;
    DoctorRepository doctorRepository;
    PatientRepository patientRepository;

    @GetMapping
    public ResponseEntity<List<MedicalReport>> getAllMedicalReports() {
        List<MedicalReport> MedicalReports = medicalReportRepository.findAll();
        return new ResponseEntity<>(MedicalReports, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalReport> getMedicalReportById(@PathVariable Long id) {
        Optional<MedicalReport> medicalReport = medicalReportRepository.findById(id);
        return medicalReport.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<MedicalReport> createMedicalReport(@RequestBody MedicalReport medicalReport) {

        Doctor doctor =     doctorRepository.findById(medicalReport.getDoctor().getId()).orElse(null);
        Patient patient =     patientRepository.findById(medicalReport.getPatient().getId()).orElse(null);

        medicalReport.setDoctor(doctor);
        medicalReport.setPatient(patient);

        MedicalReport savedMedicalReport = medicalReportRepository.save(medicalReport);
        return new ResponseEntity<>(savedMedicalReport, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalReport> updateMedicalReport(@PathVariable Long id, @RequestBody MedicalReport medicalReport) {
        Optional<MedicalReport> existingMedicalReport = medicalReportRepository.findById(id);
        if (existingMedicalReport.isPresent()) {

            MedicalReport updatedMedicalReport = existingMedicalReport.get();

            updatedMedicalReport.setPrescription(medicalReport.getPrescription());
            updatedMedicalReport.setDocument(medicalReport.getDocument());
            updatedMedicalReport.setPatient(patientRepository.findById(medicalReport.getpId()).orElse(null));
            updatedMedicalReport.setDoctor(doctorRepository.findById(medicalReport.getDocId()).orElse(null));

            return new ResponseEntity<>(medicalReportRepository.save(updatedMedicalReport), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMedicalReport(@PathVariable Long id) {
        try {
            medicalReportRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}


