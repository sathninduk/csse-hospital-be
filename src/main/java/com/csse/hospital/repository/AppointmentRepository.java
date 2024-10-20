// AppointmentRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :id ORDER BY a.id")
    List<Appointment> findByDoctorId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :id ORDER BY a.id")
    List<Appointment> findByPatientId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.status = :status ORDER BY a.id")
    List<Appointment> findByStatus(@Param("status") int status, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentTime BETWEEN :start AND :end ORDER BY a.id")
    List<Appointment> findByDateRange(@Param("start") Timestamp start, @Param("end") Timestamp end, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentTime BETWEEN :start AND :end AND " +
            "(:key = 'id' AND a.id = :value OR " +
            ":key = 'doctor_id' AND a.doctor.id = :value OR " +
            ":key = 'patient_id' AND a.patient.id = :value)")
    long countByKeyAndValueAndDateRange(@Param("key") String key,
                                        @Param("value") String value,
                                        @Param("start") Timestamp start,
                                        @Param("end") Timestamp end);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentTime BETWEEN :start AND :end")
    long countByDateRange(@Param("start") Timestamp start,
                          @Param("end") Timestamp end);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE " +
            "(:key = 'id' AND a.id = :value OR " +
            ":key = 'doctor_id' AND a.doctor.id = :value OR " +
            ":key = 'patient_id' AND a.patient.id = :value)")
    long countByKeyAndValue(@Param("key") String key,
                            @Param("value") String value);
}