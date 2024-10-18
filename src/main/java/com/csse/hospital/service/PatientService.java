// PatientService.java
package com.csse.hospital.service;

import com.csse.hospital.model.user.Patient;
import com.csse.hospital.model.user.Role;
import com.csse.hospital.repository.PatientRepository;
import com.csse.hospital.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Patient createPatient(Patient patient) {
        Role patientRole = roleRepository.findById(1L).orElseThrow(() -> new RuntimeException("Role not found"));
        patient.setRoles(Collections.singleton(patientRole));
        return patientRepository.save(patient);
    }
}