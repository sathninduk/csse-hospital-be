// HealthCardController.java
package com.csse.hospital.controller;

import com.csse.hospital.model.HealthCard;
import com.csse.hospital.service.HealthCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/healthcards")
public class HealthCardController {

    @Autowired
    private HealthCardService healthCardService;

    @GetMapping
    public ResponseEntity<List<HealthCard>> getAllHealthCards() {
        List<HealthCard> healthCards = healthCardService.getAllHealthCards();
        return new ResponseEntity<>(healthCards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HealthCard> getHealthCardById(@PathVariable Long id) {
        Optional<HealthCard> healthCard = healthCardService.getHealthCardById(id);
        return healthCard.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<HealthCard> createHealthCard(@RequestBody HealthCard healthCard) {
        HealthCard savedHealthCard = healthCardService.createHealthCard(healthCard);
        return new ResponseEntity<>(savedHealthCard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthCard> updateHealthCard(@PathVariable Long id, @RequestBody HealthCard healthCard) {
        Optional<HealthCard> existingHealthCard = healthCardService.getHealthCardById(id);
        if (existingHealthCard.isPresent()) {
            HealthCard updatedHealthCard = existingHealthCard.get();
            updatedHealthCard.setCardNumber(healthCard.getCardNumber());
            updatedHealthCard.setPatient(healthCard.getPatient());
            return new ResponseEntity<>(healthCardService.updateHealthCard(updatedHealthCard), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteHealthCard(@PathVariable Long id) {
        try {
            healthCardService.deleteHealthCard(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}