// CreditCardRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.paymentMethod.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}