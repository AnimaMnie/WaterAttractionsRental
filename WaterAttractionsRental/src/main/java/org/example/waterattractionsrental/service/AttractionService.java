package org.example.waterattractionsrental.service;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.entity.Attraction;
import org.example.waterattractionsrental.repository.AttractionRepository;
import org.springframework.stereotype.Service;
import org.example.waterattractionsrental.dto.ReservationDTO;
import org.example.waterattractionsrental.dto.AttractionDTO;
import org.example.waterattractionsrental.entity.AttractionType;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttractionService {

    private final AttractionRepository attractionRepository;

    public List<AttractionDTO> getAllAttractions() {
        return attractionRepository.findAll().stream()
                .map(attraction -> AttractionDTO.builder()
                        .id(attraction.getId())
                        .name(attraction.getName())
                        .type(attraction.getType())
                        .price(attraction.getPrice())
                        .available(attraction.isAvailable())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public Optional<AttractionDTO> getAttractionById(Long id) {
        return attractionRepository.findById(id)
                .map(attraction -> AttractionDTO.builder()
                        .id(attraction.getId())
                        .name(attraction.getName())
                        .type(attraction.getType())
                        .available(attraction.isAvailable())
                        .price(attraction.getPrice())
                        .build()
                );
    }

    public Attraction save(Attraction attraction) {
        return attractionRepository.save(attraction);
    }

    public void deleteById(Long id) {
        attractionRepository.deleteById(id);
    }

    public Attraction updateAttraction(Long id, AttractionDTO dto) {
        return attractionRepository.findById(id)
                .map(existing -> {
                    if (dto.getName() != null && !dto.getName().isBlank()) {
                        existing.setName(dto.getName());
                    }

                    if (dto.getType() != null) {
                        existing.setType(dto.getType());
                    }

                    if (dto.getAvailable() != null) {
                        existing.setAvailable(dto.getAvailable());
                    }

                    if (dto.getPrice() != null) {
                        existing.setPrice(dto.getPrice());
                    }

                    return attractionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Attraction not found"));
    }
}
