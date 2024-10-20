// AppointmentService.java
package com.csse.hospital.service;

import com.csse.hospital.model.Appointment;
import com.csse.hospital.repository.AppointmentRepository;
import com.csse.hospital.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments(int page, int size) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id"));
        return appointmentRepository.findAll(pageable).getContent();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, Appointment appointment) {
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
        if (existingAppointment.isPresent()) {
            Appointment updatedAppointment = existingAppointment.get();
            updatedAppointment.setAppointmentTime(appointment.getAppointmentTime());
            updatedAppointment.setDoctor(appointment.getDoctor());
            updatedAppointment.setPatient(appointment.getPatient());
            return appointmentRepository.save(updatedAppointment);
        } else {
            return null;
        }
    }

    public List<Appointment> updateAppointmentsBulk(Map<Long, Appointment> appointmentsToUpdate) {
        List<Appointment> updatedAppointments = new ArrayList<>();
        for (Map.Entry<Long, Appointment> entry : appointmentsToUpdate.entrySet()) {
            Long id = entry.getKey();
            Appointment appointment = entry.getValue();
            Optional<Appointment> existingAppointmentOpt = appointmentRepository.findById(id);
            if (existingAppointmentOpt.isPresent()) {
                Appointment existingAppointment = existingAppointmentOpt.get();
                if (appointment.getAppointmentTime() != null) {
                    existingAppointment.setAppointmentTime(appointment.getAppointmentTime());
                }
                if (appointment.getDoctor() != null) {
                    existingAppointment.setDoctor(appointment.getDoctor());
                }
                if (appointment.getPatient() != null) {
                    existingAppointment.setPatient(appointment.getPatient());
                }
                if (appointment.getStatus() != 0) {
                    existingAppointment.setStatus(appointment.getStatus());
                }
                if (appointment.getAppointmentTime() != null) {
                    existingAppointment.setAppointmentTime(appointment.getAppointmentTime());
                }
                updatedAppointments.add(appointmentRepository.save(existingAppointment));
            }
        }
        return updatedAppointments;
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public void deleteAppointmentsBulk(List<Long> ids) {
        appointmentRepository.deleteAllById(ids);
    }

    public List<Appointment> searchAppointments(String key, String value, int page, int size, String start, String end) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustedPage, size);

        if (start != null && end != null) {
            Timestamp[] timestamps = DateUtil.convertToTimestamps(start, end);
            return appointmentRepository.findByDateRange(timestamps[0], timestamps[1], pageable);
        }

        switch (key) {
            case "id":
                return appointmentRepository.findById(Long.parseLong(value)).map(List::of).orElse(List.of());
            case "doctor_id":
                return appointmentRepository.findByDoctorId(Long.parseLong(value), pageable);
            case "patient_id":
                return appointmentRepository.findByPatientId(Long.parseLong(value), pageable);
            case "status":
                return appointmentRepository.findByStatus(Integer.parseInt(value), pageable);
            default:
                return List.of();
        }
    }

    public long getAppointmentsCount(String key, String value, String start, String end) {
        if (key != null && value != null && start != null && end != null) {
            Timestamp[] timestamps = DateUtil.convertToTimestamps(start, end);
            return appointmentRepository.countByKeyAndValueAndDateRange(key, value, timestamps[0], timestamps[1]);
        } else if (key != null && value != null) {
            return appointmentRepository.countByKeyAndValue(key, value);
        } else if (start != null && end != null) {
            Timestamp[] timestamps = DateUtil.convertToTimestamps(start, end);
            return appointmentRepository.countByDateRange(timestamps[0], timestamps[1]);
        } else {
            return appointmentRepository.count();
        }
    }
}