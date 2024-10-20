// AppointmentsController.java
package com.csse.hospital.controller;

import com.csse.hospital.model.Appointment;
import com.csse.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentsController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) String key, @RequestParam(required = false) String value) {
        if (key != null && value != null && !value.isEmpty()) {
            List<Appointment> appointments = appointmentService.searchAppointments(key, value, page, size, null, null);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        }
        List<Appointment> appointments = appointmentService.getAllAppointments(page, size);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        return appointment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        // convert "appointmentDate" to timestamp
        appointment.setAppointmentTime(appointment.getAppointmentTime());

        Appointment savedAppointment = appointmentService.createAppointment(appointment);
        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
        if (updatedAppointment != null) {
            return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/bulk")
    public ResponseEntity<List<Appointment>> updateAppointmentsBulk(@RequestBody Map<Long, Appointment> appointmentsToUpdate) {
        List<Appointment> updatedAppointments = appointmentService.updateAppointmentsBulk(appointmentsToUpdate);
        return new ResponseEntity<>(updatedAppointments, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<HttpStatus> deleteAppointmentsBulk(@RequestBody List<Long> ids) {
        try {
            appointmentService.deleteAppointmentsBulk(ids);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Appointment>> searchAppointments(@RequestParam(required = false) String key, @RequestParam(required = false) String value, @RequestParam int page, @RequestParam int size, @RequestParam(required = false) String start, @RequestParam(required = false) String end) {
        List<Appointment> appointments = appointmentService.searchAppointments(key, value, page, size, start, end);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getAppointmentsCount(@RequestParam(required = false) String key,
                                                     @RequestParam(required = false) String value,
                                                     @RequestParam(required = false) String start,
                                                     @RequestParam(required = false) String end) {
        long count = appointmentService.getAppointmentsCount(key, value, start, end);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}