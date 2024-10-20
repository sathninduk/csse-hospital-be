// HealthCardService.java
package com.csse.hospital.service;

import com.csse.hospital.model.HealthCard;
import com.csse.hospital.repository.HealthCardRepository;
import com.csse.hospital.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class HealthCardService {

    @Autowired
    private HealthCardRepository healthCardRepository;

    private static HealthCardService instance;

    private HealthCardService() {}

    public static synchronized HealthCardService getInstance() {
        if (instance == null) {
            instance = new HealthCardService();
        }
        return instance;
    }

    public List<HealthCard> getAllHealthCards(int page, int size) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id"));
        return healthCardRepository.findAll(pageable).getContent();
    }

    public Optional<HealthCard> getHealthCardById(Long id) {
        return healthCardRepository.findById(id);
    }

    public HealthCard createHealthCard(HealthCard healthCard) {
        return healthCardRepository.save(healthCard);
    }

    public HealthCard updateHealthCard(Long id, HealthCard healthCard) {
        Optional<HealthCard> existingHealthCard = healthCardRepository.findById(id);
        if (existingHealthCard.isPresent()) {
            HealthCard updatedHealthCard = existingHealthCard.get();
            updatedHealthCard.setCardNumber(healthCard.getCardNumber());
            updatedHealthCard.setPatient(healthCard.getPatient());
            return healthCardRepository.save(updatedHealthCard);
        } else {
            return null;
        }
    }

    public List<HealthCard> updateHealthCardsBulk(Map<Long, HealthCard> healthCardsToUpdate) {
        List<HealthCard> updatedHealthCards = new ArrayList<>();
        for (Map.Entry<Long, HealthCard> entry : healthCardsToUpdate.entrySet()) {
            Long id = entry.getKey();
            HealthCard healthCard = entry.getValue();
            Optional<HealthCard> existingHealthCardOpt = healthCardRepository.findById(id);
            if (existingHealthCardOpt.isPresent()) {
                HealthCard existingHealthCard = existingHealthCardOpt.get();
                if (healthCard.getCardNumber() != null) {
                    existingHealthCard.setCardNumber(healthCard.getCardNumber());
                }
                if (healthCard.getPatient() != null) {
                    existingHealthCard.setPatient(healthCard.getPatient());
                }
                if (healthCard.getStatus() != 0) {
                    existingHealthCard.setStatus(healthCard.getStatus());
                }
                if (healthCard.getRequestDate() != null) {
                    existingHealthCard.setRequestDate(healthCard.getRequestDate());
                }
                updatedHealthCards.add(healthCardRepository.save(existingHealthCard));
            }
        }
        return updatedHealthCards;
    }

    public void deleteHealthCard(Long id) {
        healthCardRepository.deleteById(id);
    }

    public void deleteHealthCardsBulk(List<Long> ids) {
        healthCardRepository.deleteAllById(ids);
    }

    public List<HealthCard> searchHealthCards(String key, String value, int page, int size, String start, String end) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustedPage, size);

        if (start != null && end != null) {
            Timestamp[] timestamps = DateUtil.convertToTimestamps(start, end);
            return healthCardRepository.findByDateRange(timestamps[0], timestamps[1], pageable);
        }

        switch (key) {
            case "id":
                return healthCardRepository.findById(Long.parseLong(value)).map(List::of).orElse(List.of());
            case "patient_id":
                return healthCardRepository.findByPatientId(Long.parseLong(value), pageable);
            case "status":
                return healthCardRepository.findByStatus(Integer.parseInt(value), pageable);
            default:
                return List.of();
        }
    }

    public long getHealthCardsCount(String key, String value, String start, String end) {
        if (key != null && value != null && start != null && end != null) {
            Timestamp[] timestamps = DateUtil.convertToTimestamps(start, end);
            return healthCardRepository.countByKeyAndValueAndDateRange(key, value, timestamps[0], timestamps[1]);
        } else if (Objects.equals(key, "status") && value != null) {
            return healthCardRepository.countByStatus(Integer.parseInt(value));
        } else if (key != null && value != null) {
            return healthCardRepository.countByKeyAndValue(key, value);
        } else if (start != null && end != null) {
            Timestamp[] timestamps = DateUtil.convertToTimestamps(start, end);
            return healthCardRepository.countByDateRange(timestamps[0], timestamps[1]);
        } else {
            return healthCardRepository.count();
        }
    }
}