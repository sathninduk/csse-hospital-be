// UserRepository.java
package com.csse.hospital.repository;

import com.csse.hospital.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}