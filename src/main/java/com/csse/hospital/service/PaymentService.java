package com.csse.hospital.service;

import com.csse.hospital.model.Payment;
import com.csse.hospital.repository.PaymentRepository;
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

/***
 * Service class for Payment
 * This class contains methods to interact with the PaymentRepository
 * and perform CRUD operations on Payment entities
 */

@Service
public class PaymentService {

    // Inject the PaymentRepository
    @Autowired
    private PaymentRepository paymentRepository;

    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    // Singleton instance
    private static PaymentService instance;

    private PaymentService() {}

    public static synchronized PaymentService getInstance() {
        if (instance == null) {
            instance = new PaymentService();
        }
        return instance;
    }

    // Payment update strategy
    private PaymentUpdateStrategy paymentUpdateStrategy = new DefaultPaymentUpdateStrategy();

    public void setPaymentUpdateStrategy(PaymentUpdateStrategy paymentUpdateStrategy) {
        this.paymentUpdateStrategy = paymentUpdateStrategy;
    }

    // Get all payments with pagination
    public List<Payment> getAllPayments(int page, int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id"));
            return paymentRepository.findAll(pageable).getContent();
        } catch (Exception e) {
            logger.error("Error getting all payments", e);
            return Collections.emptyList();
        }
    }

    // Get payment by ID
    public Optional<Payment> getPaymentById(Long id) {
        try {
            return paymentRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error getting payment by id: {}", id, e);
            return Optional.empty();
        }
    }

    // Create a new payment
    public Payment createPayment(Payment payment) {
        try {
            return paymentRepository.save(payment);
        } catch (Exception e) {
            logger.error("Error creating payment", e);
            return null;
        }
    }

    // Update an existing payment
    public Payment updatePayment(Long id, Payment payment) {
        try {
            Optional<Payment> existingPaymentOpt = paymentRepository.findById(id);
            if (existingPaymentOpt.isPresent()) {
                Payment existingPayment = existingPaymentOpt.get();
                existingPayment = paymentUpdateStrategy.updatePayment(existingPayment, payment);
                return paymentRepository.save(existingPayment);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Error updating payment with id: {}", id, e);
            return null;
        }
    }

    // Update payments in bulk
    public List<Payment> updatePaymentsBulk(Map<Long, Payment> paymentsToUpdate) {
        List<Payment> updatedPayments = new ArrayList<>();
        try {
            for (Map.Entry<Long, Payment> entry : paymentsToUpdate.entrySet()) {
                Long id = entry.getKey();
                Payment payment = entry.getValue();
                Optional<Payment> existingPaymentOpt = paymentRepository.findById(id);
                if (existingPaymentOpt.isPresent()) {
                    Payment existingPayment = existingPaymentOpt.get();
                    existingPayment = paymentUpdateStrategy.updatePayment(existingPayment, payment);
                    updatedPayments.add(paymentRepository.save(existingPayment));
                }
            }
        } catch (Exception e) {
            logger.error("Error updating payments in bulk", e);
        }
        return updatedPayments;
    }

    // Delete a payment by ID
    public void deletePayment(Long id) {
        try {
            paymentRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting payment with id: {}", id, e);
        }
    }

    // Delete payments in bulk
    public void deletePaymentsBulk(List<Long> ids) {
        try {
            paymentRepository.deleteAllById(ids);
        } catch (Exception e) {
            logger.error("Error deleting payments in bulk: {}", ids, e);
        }
    }

    // Search payments with various filters
    public List<Payment> searchPayments(String key, String value, int page, int size, String start, String end) {
        try {
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
        } catch (Exception e) {
            logger.error("Error searching payments with key: {}, value: {}", key, value, e);
            return Collections.emptyList();
        }
    }

    // Get the count of payments with various filters
    public long getPaymentsCount(String key, String value, String start, String end) {
        try {
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
        } catch (Exception e) {
            logger.error("Error getting payments count with key: {}, value: {}", key, value, e);
            return 0;
        }
    }
}