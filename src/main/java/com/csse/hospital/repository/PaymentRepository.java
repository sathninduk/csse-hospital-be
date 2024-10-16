// PaymentRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}