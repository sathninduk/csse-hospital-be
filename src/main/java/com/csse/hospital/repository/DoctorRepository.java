// DoctorRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.user.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}