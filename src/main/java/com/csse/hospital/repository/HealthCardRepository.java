// HealthCardRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.HealthCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCardRepository extends JpaRepository<HealthCard, Long> {
}