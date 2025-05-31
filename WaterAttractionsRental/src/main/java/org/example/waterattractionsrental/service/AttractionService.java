package org.example.waterattractionsrental.service;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.entity.Attraction;
import org.example.waterattractionsrental.repository.AttractionRepository;
import org.springframework.stereotype.Service;
import org.example.waterattractionsrental.dto.ReservationDTO;
import org.example.waterattractionsrental.dto.AttractionDTO;
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
                        .available(attraction.isAvailable())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public Optional<Attraction> findById(Long id) {
        return attractionRepository.findById(id);
    }

    public Attraction save(Attraction attraction) {
        return attractionRepository.save(attraction);
    }

    public void deleteById(Long id) {
        attractionRepository.deleteById(id);
    }
}
