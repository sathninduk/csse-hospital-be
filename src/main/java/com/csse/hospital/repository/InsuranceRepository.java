// InsuranceRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.paymentMethod.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
}