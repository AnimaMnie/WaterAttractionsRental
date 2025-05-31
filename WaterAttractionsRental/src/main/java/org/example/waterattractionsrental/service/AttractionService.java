package org.example.waterattractionsrental.service;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.entity.Attraction;
import org.example.waterattractionsrental.repository.AttractionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttractionService {

    private final AttractionRepository attractionRepository;

    public List<Attraction> findAll() {
        return attractionRepository.findAll();
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
