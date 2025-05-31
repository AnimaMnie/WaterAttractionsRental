package org.example.waterattractionsrental.controller;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.entity.Attraction;
import org.example.waterattractionsrental.service.AttractionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/attractions")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Attraction> getAll() {
        return attractionService.findAll();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        attractionService.deleteById(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Attraction> getAttractionById(@PathVariable Long id) {
        return attractionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Attraction> createAttraction(@RequestBody Attraction attraction) {
        return ResponseEntity.ok(attractionService.save(attraction));
    }
}
