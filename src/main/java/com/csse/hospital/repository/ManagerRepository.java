// ManagerRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.user.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
}