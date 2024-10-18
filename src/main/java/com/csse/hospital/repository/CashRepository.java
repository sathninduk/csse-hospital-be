// CashRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.paymentMethod.Cash;
import com.csse.hospital.model.paymentMethod.Cash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashRepository extends JpaRepository<Cash, Long> {
}