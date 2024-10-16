// PatientRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.user.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}