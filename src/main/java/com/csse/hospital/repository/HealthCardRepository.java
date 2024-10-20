// HealthCardRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.HealthCard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface HealthCardRepository extends JpaRepository<HealthCard, Long> {
    @Query("SELECT h FROM HealthCard h WHERE h.patient.id = :id ORDER BY h.id")
    List<HealthCard> findByPatientId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT h FROM HealthCard h WHERE h.requestDate BETWEEN :start AND :end ORDER BY h.id")
    List<HealthCard> findByDateRange(@Param("start") Timestamp start, @Param("end") Timestamp end, Pageable pageable);

    // findByStatus
    @Query("SELECT h FROM HealthCard h WHERE h.status = :value ORDER BY h.id")
    List<HealthCard> findByStatus(@Param("value") int value, Pageable pageable);

    // findByStatusCount
    @Query("SELECT COUNT(h) FROM HealthCard h WHERE h.status = :value")
    long countByStatus(@Param("value") int value);

    @Query("SELECT COUNT(h) FROM HealthCard h WHERE h.requestDate BETWEEN :start AND :end AND " +
            "(:key = 'id' AND h.id = :value OR " +
            ":key = 'patient_id' AND h.patient.id = :value)")
    long countByKeyAndValueAndDateRange(@Param("key") String key,
                                        @Param("value") String value,
                                        @Param("start") Timestamp start,
                                        @Param("end") Timestamp end);

    @Query("SELECT COUNT(h) FROM HealthCard h WHERE h.requestDate BETWEEN :start AND :end")
    long countByDateRange(@Param("start") Timestamp start,
                          @Param("end") Timestamp end);

    @Query("SELECT COUNT(h) FROM HealthCard h WHERE " +
            "(:key = 'id' AND h.id = :value OR " +
            ":key = 'patient_id' AND h.patient.id = :value)")
    long countByKeyAndValue(@Param("key") String key,
                            @Param("value") String value);
}