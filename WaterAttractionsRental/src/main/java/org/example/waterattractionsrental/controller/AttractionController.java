package org.example.waterattractionsrental.controller;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.entity.Attraction;
import org.example.waterattractionsrental.service.AttractionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.example.waterattractionsrental.dto.UserDTO;
import org.example.waterattractionsrental.dto.AttractionDTO;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<AttractionDTO> getAllAttractions() {
        return attractionService.getAllAttractions();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        attractionService.deleteById(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AttractionDTO> getById(@PathVariable Long id) {
        return attractionService.getAttractionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Attraction> createAttraction(@RequestBody Attraction attraction) {
        return ResponseEntity.ok(attractionService.save(attraction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attraction> updateAttraction(@PathVariable Long id, @RequestBody AttractionDTO dto) {
        Attraction updated = attractionService.updateAttraction(id, dto);
        return ResponseEntity.ok(updated);
    }
}
