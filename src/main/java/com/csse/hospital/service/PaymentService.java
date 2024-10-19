package com.csse.hospital.service;

import com.csse.hospital.model.Payment;
import com.csse.hospital.repository.PaymentRepository;
import com.csse.hospital.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> getAllPayments(int page, int size) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id"));
        return paymentRepository.findAll(pageable).getContent();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long id, Payment payment) {
        Optional<Payment> existingPayment = paymentRepository.findById(id);
        if (existingPayment.isPresent()) {
            payment.setId(id);
            return paymentRepository.save(payment);
        } else {
            return null;
        }
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public void deletePaymentsBulk(List<Long> ids) {
        paymentRepository.deleteAllById(ids);
    }

    public List<Payment> searchPayments(String key, String value, int page, int size) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustedPage, size);
        switch (key) {
            case "id":
                return paymentRepository.findById(Long.parseLong(value)).map(List::of).orElse(List.of());
            case "patient_id":
                return paymentRepository.findByPatientId(Long.parseLong(value), pageable);
            case "payment_method_id":
                return paymentRepository.findByPaymentMethodId(Long.parseLong(value), pageable);
            default:
                return List.of();
        }
    }

    public long getPaymentsCount(String key, String value, String start, String end) {
        if (key != null && value != null && start != null && end != null) {
            Timestamp[] timestamps = DateUtil.convertToTimestamps(start, end);
            return paymentRepository.countByKeyAndValueAndDateRange(key, value, timestamps[0], timestamps[1]);
        } else if (key != null && value != null) {
            return paymentRepository.countByKeyAndValue(key, value);
        } else if (start != null && end != null) {
            Timestamp[] timestamps = DateUtil.convertToTimestamps(start, end);
            return paymentRepository.countByDateRange(timestamps[0], timestamps[1]);
        } else {
            return paymentRepository.count();
        }
    }
}