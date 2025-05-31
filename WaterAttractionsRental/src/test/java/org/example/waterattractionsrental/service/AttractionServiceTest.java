package org.example.waterattractionsrental.service;

import org.example.waterattractionsrental.dto.AttractionDTO;
import org.example.waterattractionsrental.entity.Attraction;
import org.example.waterattractionsrental.entity.AttractionType;
import org.example.waterattractionsrental.repository.AttractionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttractionServiceTest {

    @Mock
    private AttractionRepository attractionRepository;

    @InjectMocks
    private AttractionService attractionService;

    private Attraction attraction;

    @BeforeEach
    void setUp() {
        attraction = new Attraction();
        attraction.setId(1L);
        attraction.setName("Kajak");
        attraction.setType(AttractionType.KAJAK);
        attraction.setPrice(120.0);
        attraction.setAvailable(true);
    }

    @Test
    void getAllAttractions_shouldReturnDTOs() {
        when(attractionRepository.findAll()).thenReturn(List.of(attraction));
        List<AttractionDTO> result = attractionService.getAllAttractions();
        assertEquals(1, result.size());
        assertEquals("Kajak", result.get(0).getName());
        assertEquals(AttractionType.KAJAK, result.get(0).getType());
        verify(attractionRepository).findAll();
    }

    @Test
    void getAttractionById_shouldReturnDTOIfExists() {
        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));
        Optional<AttractionDTO> result = attractionService.getAttractionById(1L);
        assertTrue(result.isPresent());
        assertEquals("Kajak", result.get().getName());
        assertEquals(AttractionType.KAJAK, result.get().getType());
    }

    @Test
    void getAttractionById_shouldReturnEmptyIfNotExists() {
        when(attractionRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<AttractionDTO> result = attractionService.getAttractionById(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void save_shouldPersistAttraction() {
        when(attractionRepository.save(attraction)).thenReturn(attraction);
        Attraction saved = attractionService.save(attraction);
        assertNotNull(saved);
        assertEquals("Kajak", saved.getName());
        verify(attractionRepository).save(attraction);
    }

    @Test
    void deleteById_shouldCallRepository() {
        attractionService.deleteById(1L);
        verify(attractionRepository).deleteById(1L);
    }

    @Test
    void updateAttraction_shouldUpdateFields() {
        AttractionDTO dto = AttractionDTO.builder()
                .name("Nowy Kajak")
                .type(AttractionType.ROWER_WODNY)
                .price(150.0)
                .available(false)
                .build();

        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));
        when(attractionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Attraction updated = attractionService.updateAttraction(1L, dto);

        assertEquals("Nowy Kajak", updated.getName());
        assertEquals(AttractionType.ROWER_WODNY, updated.getType());
        assertEquals(150.0, updated.getPrice());
        assertFalse(updated.isAvailable());
    }

    @Test
    void updateAttraction_shouldThrowIfNotFound() {
        AttractionDTO dto = AttractionDTO.builder().build();
        when(attractionRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> attractionService.updateAttraction(123L, dto));
    }
}
