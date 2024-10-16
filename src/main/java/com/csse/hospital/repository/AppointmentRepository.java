// AppointmentRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}