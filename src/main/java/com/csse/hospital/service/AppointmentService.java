// AppointmentService.java
package com.csse.hospital.service;

import com.csse.hospital.model.Appointment;
import com.csse.hospital.repository.AppointmentRepository;
import com.csse.hospital.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private static AppointmentService instance;

    // Private constructor to prevent instantiation
    private AppointmentService() {}

    // Singleton pattern to get the instance of AppointmentService
    public static synchronized AppointmentService getInstance() {
        if (instance == null) {
            instance = new AppointmentService();
        }
        return instance;
    }

    // Get all appointments with pagination
    public List<Appointment> getAllAppointments(int page, int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id"));
            return appointmentRepository.findAll(pageable).getContent();
        } catch (Exception e) {
            logger.error("Error fetching all appointments", e);
            return Collections.emptyList();
        }
    }

    // Get appointment by ID
    public Optional<Appointment> getAppointmentById(Long id) {
        try {
            return appointmentRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error fetching appointment by id: {}", id, e);
            return Optional.empty();
        }
    }

    // Create a new appointment
    public Appointment createAppointment(Appointment appointment) {
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            logger.error("Error creating appointment: {}", appointment, e);
            return null;
        }
    }

    // Update an existing appointment
    public Appointment updateAppointment(Long id, Appointment appointment) {
        try {
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
        } catch (Exception e) {
            logger.error("Error updating appointment with id: {}", id, e);
            return null;
        }
    }

    // Update appointments in bulk
    public List<Appointment> updateAppointmentsBulk(Map<Long, Appointment> appointmentsToUpdate) {
        List<Appointment> updatedAppointments = new ArrayList<>();
        try {
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
                    updatedAppointments.add(appointmentRepository.save(existingAppointment));
                }
            }
        } catch (Exception e) {
            logger.error("Error updating appointments in bulk", e);
        }
        return updatedAppointments;
    }

    // Delete an appointment by ID
    public void deleteAppointment(Long id) {
        try {
            appointmentRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting appointment with id: {}", id, e);
        }
    }

    // Delete appointments in bulk
    public void deleteAppointmentsBulk(List<Long> ids) {
        try {
            appointmentRepository.deleteAllById(ids);
        } catch (Exception e) {
            logger.error("Error deleting appointments in bulk: {}", ids, e);
        }
    }

    // Search appointments with various filters
    public List<Appointment> searchAppointments(String key, String value, int page, int size, String start, String end) {
        try {
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
        } catch (Exception e) {
            logger.error("Error searching appointments with key: {}, value: {}", key, value, e);
            return Collections.emptyList();
        }
    }

    // Get the count of appointments with various filters
    public long getAppointmentsCount(String key, String value, String start, String end) {
        try {
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
        } catch (Exception e) {
            logger.error("Error getting appointments count with key: {}, value: {}", key, value, e);
            return 0;
        }
    }
}