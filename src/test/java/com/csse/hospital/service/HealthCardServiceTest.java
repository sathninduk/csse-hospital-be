package com.csse.hospital.service;

import com.csse.hospital.model.HealthCard;
import com.csse.hospital.repository.HealthCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HealthCardServiceTest {

    @Mock
    private HealthCardRepository healthCardRepository;

    @InjectMocks
    private HealthCardService healthCardService;

    private HealthCard healthCard;

    @BeforeEach
    public void setUp() {
        healthCard = new HealthCard();
        healthCard.setId(1L);
        healthCard.setCardNumber("1234567890");
        healthCard.setStatus(1);
    }

    @Test
    public void testGetAllHealthCards_Positive() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        List<HealthCard> healthCards = List.of(healthCard);
        Page<HealthCard> page = new PageImpl<>(healthCards);
        doReturn(page).when(healthCardRepository).findAll(pageable);

        List<HealthCard> result = healthCardService.getAllHealthCards(1, 10);
        assertEquals(1, result.size());
        assertEquals(healthCard, result.get(0));
    }

    @Test
    public void testGetAllHealthCards_Negative() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        doReturn(Page.empty()).when(healthCardRepository).findAll(pageable);

        List<HealthCard> result = healthCardService.getAllHealthCards(1, 10);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetHealthCardById_Positive() {
        when(healthCardRepository.findById(1L)).thenReturn(Optional.of(healthCard));

        Optional<HealthCard> result = healthCardService.getHealthCardById(1L);
        assertTrue(result.isPresent());
        assertEquals(healthCard, result.get());
    }

    @Test
    public void testGetHealthCardById_Negative() {
        when(healthCardRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<HealthCard> result = healthCardService.getHealthCardById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testCreateHealthCard_Positive() {
        when(healthCardRepository.save(any(HealthCard.class))).thenReturn(healthCard);

        HealthCard result = healthCardService.createHealthCard(healthCard);
        assertEquals(healthCard, result);
    }

    @Test
    public void testCreateHealthCard_Negative() {
        when(healthCardRepository.save(any(HealthCard.class))).thenThrow(new RuntimeException("Error creating health card"));

        HealthCard result = healthCardService.createHealthCard(healthCard);
        assertNull(result);
    }

    @Test
    public void testUpdateHealthCard_Positive() {
        when(healthCardRepository.findById(1L)).thenReturn(Optional.of(healthCard));
        when(healthCardRepository.save(any(HealthCard.class))).thenReturn(healthCard);

        HealthCard updatedHealthCard = new HealthCard();
        updatedHealthCard.setCardNumber("0987654321");
        updatedHealthCard.setStatus(2);

        HealthCard result = healthCardService.updateHealthCard(1L, updatedHealthCard);
        assertNotNull(result);
        assertEquals("0987654321", result.getCardNumber());
        assertEquals(1, result.getStatus());
    }

    @Test
    public void testUpdateHealthCard_Negative() {
        when(healthCardRepository.findById(1L)).thenReturn(Optional.empty());

        HealthCard updatedHealthCard = new HealthCard();
        updatedHealthCard.setCardNumber("0987654321");
        updatedHealthCard.setStatus(2);

        HealthCard result = healthCardService.updateHealthCard(1L, updatedHealthCard);
        assertNull(result);
    }

    @Test
    public void testUpdateHealthCardsBulk_Positive() {
        Map<Long, HealthCard> healthCardsToUpdate = new HashMap<>();
        healthCardsToUpdate.put(1L, healthCard);

        when(healthCardRepository.findById(1L)).thenReturn(Optional.of(healthCard));
        when(healthCardRepository.save(any(HealthCard.class))).thenReturn(healthCard);

        List<HealthCard> result = healthCardService.updateHealthCardsBulk(healthCardsToUpdate);
        assertEquals(1, result.size());
        assertEquals(healthCard, result.get(0));
    }

    @Test
    public void testUpdateHealthCardsBulk_Negative() {
        Map<Long, HealthCard> healthCardsToUpdate = new HashMap<>();
        healthCardsToUpdate.put(1L, healthCard);

        when(healthCardRepository.findById(1L)).thenReturn(Optional.empty());

        List<HealthCard> result = healthCardService.updateHealthCardsBulk(healthCardsToUpdate);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteHealthCard_Positive() {
        doNothing().when(healthCardRepository).deleteById(1L);

        healthCardService.deleteHealthCard(1L);
        verify(healthCardRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteHealthCardsBulk_Positive() {
        List<Long> ids = List.of(1L);
        doNothing().when(healthCardRepository).deleteAllById(ids);

        healthCardService.deleteHealthCardsBulk(ids);
        verify(healthCardRepository, times(1)).deleteAllById(ids);
    }

}