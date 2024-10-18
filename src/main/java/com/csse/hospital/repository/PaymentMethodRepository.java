// PaymentMethodRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.paymentMethod.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}