// HealthCardController.java
package com.csse.hospital.controller;

import com.csse.hospital.model.HealthCard;
import com.csse.hospital.service.HealthCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// Import the logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/healthcards")
public class HealthCardController {

    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(HealthCardController.class);

    @Autowired
    private HealthCardService healthCardService;

    @GetMapping
    public ResponseEntity<List<HealthCard>> getAllHealthCards(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) String key, @RequestParam(required = false) String value) {
        logger.info("Fetching all health cards with page: {}, size: {}, key: {}, value: {}", page, size, key, value);
        if (key != null && value != null && !value.isEmpty()) {
            List<HealthCard> healthCards = healthCardService.searchHealthCards(key, value, page, size, null, null);
            return new ResponseEntity<>(healthCards, HttpStatus.OK);
        }
        List<HealthCard> healthCards = healthCardService.getAllHealthCards(page, size);
        return new ResponseEntity<>(healthCards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HealthCard> getHealthCardById(@PathVariable Long id) {
        logger.info("Fetching health card with id: {}", id);
        Optional<HealthCard> healthCard = healthCardService.getHealthCardById(id);
        return healthCard.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<HealthCard> createHealthCard(@RequestBody HealthCard healthCard) {
        logger.info("Creating new health card: {}", healthCard);
        HealthCard createdHealthCard = healthCardService.createHealthCard(healthCard);
        return new ResponseEntity<>(createdHealthCard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthCard> updateHealthCard(@PathVariable Long id, @RequestBody HealthCard healthCard) {
        logger.info("Updating health card with id: {}", id);
        HealthCard updatedHealthCard = healthCardService.updateHealthCard(id, healthCard);
        if (updatedHealthCard != null) {
            return new ResponseEntity<>(updatedHealthCard, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/bulk")
    public ResponseEntity<List<HealthCard>> updateHealthCardsBulk(@RequestBody Map<Long, HealthCard> healthCardsToUpdate) {
        logger.info("Updating health cards in bulk: {}", healthCardsToUpdate.keySet());
        List<HealthCard> updatedHealthCards = healthCardService.updateHealthCardsBulk(healthCardsToUpdate);
        return new ResponseEntity<>(updatedHealthCards, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteHealthCard(@PathVariable Long id) {
        logger.info("Deleting health card with id: {}", id);
        try {
            healthCardService.deleteHealthCard(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error deleting health card with id: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<HttpStatus> deleteHealthCardsBulk(@RequestBody List<Long> ids) {
        logger.info("Deleting health cards in bulk: {}", ids);
        try {
            healthCardService.deleteHealthCardsBulk(ids);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error deleting health cards in bulk: {}", ids, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<HealthCard>> searchHealthCards(@RequestParam(required = false) String key, @RequestParam(required = false) String value, @RequestParam int page, @RequestParam int size, @RequestParam(required = false) String start, @RequestParam(required = false) String end) {
        logger.info("Searching health cards with key: {}, value: {}, page: {}, size: {}, start: {}, end: {}", key, value, page, size, start, end);
        List<HealthCard> healthCards = healthCardService.searchHealthCards(key, value, page, size, start, end);
        return new ResponseEntity<>(healthCards, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getHealthCardsCount(@RequestParam(required = false) String key,
                                                    @RequestParam(required = false) String value,
                                                    @RequestParam(required = false) String start,
                                                    @RequestParam(required = false) String end) {
        logger.info("Getting health cards count with key: {}, value: {}, start: {}, end: {}", key, value, start, end);
        long count = healthCardService.getHealthCardsCount(key, value, start, end);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}