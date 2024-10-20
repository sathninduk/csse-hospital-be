// AppointmentServiceTest.java
package com.csse.hospital.service;

import com.csse.hospital.model.Appointment;
import com.csse.hospital.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment;

    @BeforeEach
    public void setUp() {
        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(1);
    }

    @Test
    public void testGetAllAppointments() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        List<Appointment> appointments = List.of(appointment);
        Page<Appointment> page = new PageImpl<>(appointments);
        doReturn(page).when(appointmentRepository).findAll(pageable);

        List<Appointment> result = appointmentService.getAllAppointments(1, 10);
        assertEquals(1, result.size());
        assertEquals(appointment, result.get(0));
    }

    @Test
    public void testGetAppointmentById() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Optional<Appointment> result = appointmentService.getAppointmentById(1L);
        assertTrue(result.isPresent());
        assertEquals(appointment, result.get());
    }

    @Test
    public void testCreateAppointment() {
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment result = appointmentService.createAppointment(appointment);
        assertEquals(appointment, result);
    }

    @Test
    public void testUpdateAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setStatus(2);

        Appointment result = appointmentService.updateAppointment(1L, updatedAppointment);
        assertNotNull(result);
        assertEquals(1, result.getStatus());
    }

    @Test
    public void testUpdateAppointmentsBulk() {
        Map<Long, Appointment> appointmentsToUpdate = new HashMap<>();
        appointmentsToUpdate.put(1L, appointment);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        List<Appointment> result = appointmentService.updateAppointmentsBulk(appointmentsToUpdate);
        assertEquals(1, result.size());
        assertEquals(appointment, result.get(0));
    }

    @Test
    public void testDeleteAppointment() {
        doNothing().when(appointmentRepository).deleteById(1L);

        appointmentService.deleteAppointment(1L);
        verify(appointmentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAppointmentsBulk() {
        List<Long> ids = List.of(1L);
        doNothing().when(appointmentRepository).deleteAllById(ids);

        appointmentService.deleteAppointmentsBulk(ids);
        verify(appointmentRepository, times(1)).deleteAllById(ids);
    }
}