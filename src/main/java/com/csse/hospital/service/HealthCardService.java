// HealthCardService.java
package com.csse.hospital.service;

import com.csse.hospital.model.HealthCard;
import com.csse.hospital.repository.HealthCardRepository;
import com.csse.hospital.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class HealthCardService {

    // Autowire the HealthCardRepository
    @Autowired
    private HealthCardRepository healthCardRepository;

    // Logger to log the messages
    private static final Logger logger = LoggerFactory.getLogger(HealthCardService.class);

    // Singleton instance of HealthCardService
    private static HealthCardService instance;

    // Private constructor to prevent instantiation
    private HealthCardService() {}

    // Singleton pattern to get the instance of HealthCardService
    public static synchronized HealthCardService getInstance() {
        if (instance == null) {
            instance = new HealthCardService();
        }
        return instance;
    }

    // Get all health cards with pagination
    public List<HealthCard> getAllHealthCards(int page, int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id"));
            return healthCardRepository.findAll(pageable).getContent();
        } catch (Exception e) {
            logger.error("Error fetching all health cards", e);
            return Collections.emptyList();
        }
    }

    // Get health card by ID
    public Optional<HealthCard> getHealthCardById(Long id) {
        try {
            return healthCardRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error fetching health card by id: {}", id, e);
            return Optional.empty();
        }
    }

    // Create a new health card
    public HealthCard createHealthCard(HealthCard healthCard) {
        try {
            return healthCardRepository.save(healthCard);
        } catch (Exception e) {
            logger.error("Error creating health card: {}", healthCard, e);
            return null;
        }
    }

    // Update an existing health card
    public HealthCard updateHealthCard(Long id, HealthCard healthCard) {
        try {
            Optional<HealthCard> existingHealthCard = healthCardRepository.findById(id);
            if (existingHealthCard.isPresent()) {
                HealthCard updatedHealthCard = existingHealthCard.get();
                updatedHealthCard.setCardNumber(healthCard.getCardNumber());
                updatedHealthCard.setPatient(healthCard.getPatient());
                return healthCardRepository.save(updatedHealthCard);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Error updating health card with id: {}", id, e);
            return null;
        }
    }

    // Update health cards in bulk
    public List<HealthCard> updateHealthCardsBulk(Map<Long, HealthCard> healthCardsToUpdate) {
        List<HealthCard> updatedHealthCards = new ArrayList<>();
        try {
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
        } catch (Exception e) {
            logger.error("Error updating health cards in bulk", e);
        }
        return updatedHealthCards;
    }

    // Delete a health card by ID
    public void deleteHealthCard(Long id) {
        try {
            healthCardRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting health card with id: {}", id, e);
        }
    }

    // Delete health cards in bulk
    public void deleteHealthCardsBulk(List<Long> ids) {
        try {
            healthCardRepository.deleteAllById(ids);
        } catch (Exception e) {
            logger.error("Error deleting health cards in bulk: {}", ids, e);
        }
    }

    // Search health cards with various filters
    public List<HealthCard> searchHealthCards(String key, String value, int page, int size, String start, String end) {
        try {
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
        } catch (Exception e) {
            logger.error("Error searching health cards with key: {}, value: {}", key, value, e);
            return Collections.emptyList();
        }
    }

    // Get the count of health cards with various filters
    public long getHealthCardsCount(String key, String value, String start, String end) {
        try {
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
        } catch (Exception e) {
            logger.error("Error getting health cards count with key: {}, value: {}", key, value, e);
            return 0;
        }
    }
}