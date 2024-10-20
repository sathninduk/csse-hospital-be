package com.csse.hospital.service;

import com.csse.hospital.model.Payment;
import com.csse.hospital.repository.PaymentRepository;
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
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    public void setUp() {
        payment = new Payment();
        payment.setId(1L);
        payment.setAmount(100.0);
        payment.setStatus(1);
    }

    @Test
    public void testGetAllPayments() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        List<Payment> payments = List.of(payment);
        Page<Payment> page = new PageImpl<>(payments);
        doReturn(page).when(paymentRepository).findAll(pageable);

        List<Payment> result = paymentService.getAllPayments(1, 10);
        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
    }

    @Test
    public void testGetPaymentById() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        Optional<Payment> result = paymentService.getPaymentById(1L);
        assertTrue(result.isPresent());
        assertEquals(payment, result.get());
    }

    @Test
    public void testCreatePayment() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.createPayment(payment);
        assertEquals(payment, result);
    }

    @Test
    public void testUpdatePayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment updatedPayment = new Payment();
        updatedPayment.setAmount(200.0);
        updatedPayment.setStatus(2);

        Payment result = paymentService.updatePayment(1L, updatedPayment);
        assertNotNull(result);
        assertEquals(200.0, result.getAmount());
        assertEquals(2, result.getStatus());
    }

    @Test
    public void testUpdatePaymentsBulk() {
        Map<Long, Payment> paymentsToUpdate = new HashMap<>();
        paymentsToUpdate.put(1L, payment);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        List<Payment> result = paymentService.updatePaymentsBulk(paymentsToUpdate);
        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
    }

    @Test
    public void testDeletePayment() {
        doNothing().when(paymentRepository).deleteById(1L);

        paymentService.deletePayment(1L);
        verify(paymentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePaymentsBulk() {
        List<Long> ids = List.of(1L);
        doNothing().when(paymentRepository).deleteAllById(ids);

        paymentService.deletePaymentsBulk(ids);
        verify(paymentRepository, times(1)).deleteAllById(ids);
    }
}