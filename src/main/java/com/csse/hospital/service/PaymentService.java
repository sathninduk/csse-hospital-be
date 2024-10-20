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
import java.util.*;

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
        Optional<Payment> existingPaymentOpt = paymentRepository.findById(id);
        if (existingPaymentOpt.isPresent()) {
            Payment existingPayment = existingPaymentOpt.get();

            if (payment.getAmount() != 0) {
                existingPayment.setAmount(payment.getAmount());
            }
            if (payment.getPatient() != null) {
                existingPayment.setPatient(payment.getPatient());
            }
            if (payment.getPaymentMethod() != null) {
                existingPayment.setPaymentMethod(payment.getPaymentMethod());
            }
            if (payment.getStatus() != 0) {
                existingPayment.setStatus(payment.getStatus());
            }

            return paymentRepository.save(existingPayment);
        } else {
            return null;
        }
    }

    public List<Payment> updatePaymentsBulk(Map<Long, Payment> paymentsToUpdate) {
        List<Payment> updatedPayments = new ArrayList<>();
        for (Map.Entry<Long, Payment> entry : paymentsToUpdate.entrySet()) {
            Long id = entry.getKey();
            Payment payment = entry.getValue();
            Optional<Payment> existingPaymentOpt = paymentRepository.findById(id);
            if (existingPaymentOpt.isPresent()) {
                Payment existingPayment = existingPaymentOpt.get();

                if (payment.getAmount() != 0) {
                    existingPayment.setAmount(payment.getAmount());
                }
                if (payment.getPatient() != null) {
                    existingPayment.setPatient(payment.getPatient());
                }
                if (payment.getPaymentMethod() != null) {
                    existingPayment.setPaymentMethod(payment.getPaymentMethod());
                }
                if (payment.getStatus() != 0) {
                    existingPayment.setStatus(payment.getStatus());
                }

                updatedPayments.add(paymentRepository.save(existingPayment));
            }
        }
        return updatedPayments;
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public void deletePaymentsBulk(List<Long> ids) {
        paymentRepository.deleteAllById(ids);
    }

    public List<Payment> searchPayments(String key, String value, int page, int size, String start, String end) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustedPage, size);

        if (start != null && end != null) {
            Timestamp[] timestamps = DateUtil.convertToTimestamps(start, end);
            return paymentRepository.findByDateRange(timestamps[0], timestamps[1], pageable);
        }

        switch (key) {
            case "id":
                return paymentRepository.findById(Long.parseLong(value)).map(List::of).orElse(List.of());
            case "status":
                return paymentRepository.findByStatus(Integer.parseInt(value), pageable);
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
        } else if (Objects.equals(key, "status") && value != null) {
            return paymentRepository.countByStatus(Integer.parseInt(value));
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