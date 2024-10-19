// PaymentRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.patient.id = :id ORDER BY p.id")
    List<Payment> findByPatientId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.paymentMethod.id = :id ORDER BY p.id")
    List<Payment> findByPaymentMethodId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE status = :value ORDER BY p.id")
    List<Payment> findByStatus(@Param("value") int value,
                                  Pageable pageable);

    // findByStatusAndDateRange
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :start AND :end ORDER BY p.id")
    List<Payment> findByDateRange(@Param("start") Timestamp start,
                                           @Param("end") Timestamp end,
                                           Pageable pageable);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentDate BETWEEN :start AND :end AND " +
            "(:key = 'id' AND p.id = :value OR " +
            ":key = 'patient_id' AND p.patient.id = :value OR " +
            ":key = 'payment_method_id' AND p.paymentMethod.id = :value)")
    long countByKeyAndValueAndDateRange(@Param("key") String key,
                                        @Param("value") String value,
                                        @Param("start") Timestamp start,
                                        @Param("end") Timestamp end);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentDate BETWEEN :start AND :end")
    long countByDateRange(@Param("start") Timestamp start,
                          @Param("end") Timestamp end);

    @Query("SELECT COUNT(p) FROM Payment p WHERE " +
            "(:key = 'id' AND p.id = :value OR " +
            ":key = 'patient_id' AND p.patient.id = :value OR " +
            "status = :value OR " +
            ":key = 'payment_method_id' AND p.paymentMethod.id = :value)")
    long countByKeyAndValue(@Param("key") String key,
                            @Param("value") String value);

    @Query("SELECT COUNT(p) FROM Payment p WHERE status = :value")
    long countByStatus(@Param("value") int value);
}