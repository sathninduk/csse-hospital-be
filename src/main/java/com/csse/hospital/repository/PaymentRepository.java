// PaymentRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentDate BETWEEN :start AND :end AND " +
            "(:key = 'id' AND p.id = :value OR " +
            ":key = 'user_id' AND p.patient.id = :value OR " +
            ":key = 'client_id' AND p.paymentMethod.id = :value)")
    long countByKeyAndValueAndDateRange(@Param("key") String key,
                                        @Param("value") String value,
                                        @Param("start") String start,
                                        @Param("end") String end);

    @Query("SELECT COUNT(p) FROM Payment p WHERE " +
            "(:key = 'id' AND p.id = :value OR " +
            ":key = 'user_id' AND p.patient.id = :value OR " +
            ":key = 'client_id' AND p.paymentMethod.id = :value)")
    long countByKeyAndValue(@Param("key") String key,
                            @Param("value") String value);
}