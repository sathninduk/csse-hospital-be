// HealthCardService.java
package com.csse.hospital.service;

import com.csse.hospital.model.HealthCard;
import com.csse.hospital.repository.HealthCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HealthCardService {

    @Autowired
    private HealthCardRepository healthCardRepository;

    public List<HealthCard> getAllHealthCards() {
        return healthCardRepository.findAll();
    }

    public Optional<HealthCard> getHealthCardById(Long id) {
        return healthCardRepository.findById(id);
    }

    public HealthCard createHealthCard(HealthCard healthCard) {
        return healthCardRepository.save(healthCard);
    }

    public HealthCard updateHealthCard(HealthCard healthCard) {
        return healthCardRepository.save(healthCard);
    }

    public void deleteHealthCard(Long id) {
        healthCardRepository.deleteById(id);
    }
}