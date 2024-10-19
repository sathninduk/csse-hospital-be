package com.csse.hospital.controller;

import com.csse.hospital.model.Payment;
import com.csse.hospital.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments(@RequestParam int page, @RequestParam int size) {
        List<Payment> payments = paymentService.getAllPayments(page, size);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        return payment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        Payment updatedPayment = paymentService.updatePayment(id, payment);
        if (updatedPayment != null) {
            return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/bulk")
    public ResponseEntity<List<Payment>> updatePaymentsBulk(@RequestBody Map<Long, Payment> paymentsToUpdate) {
        List<Payment> updatedPayments = paymentService.updatePaymentsBulk(paymentsToUpdate);
        return new ResponseEntity<>(updatedPayments, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePayment(@PathVariable Long id) {
        try {
            paymentService.deletePayment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<HttpStatus> deletePaymentsBulk(@RequestBody List<Long> ids) {
        try {
            paymentService.deletePaymentsBulk(ids);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Payment>> searchPayments(@RequestParam String key, @RequestParam String value, @RequestParam int page, @RequestParam int size) {
        List<Payment> payments = paymentService.searchPayments(key, value, page, size);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getPaymentsCount(@RequestParam(required = false) String key,
                                                 @RequestParam(required = false) String value,
                                                 @RequestParam(required = false) String start,
                                                 @RequestParam(required = false) String end) {
        long count = paymentService.getPaymentsCount(key, value, start, end);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}