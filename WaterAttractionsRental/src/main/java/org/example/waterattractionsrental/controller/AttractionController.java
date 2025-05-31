package org.example.waterattractionsrental.controller;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.entity.Attraction;
import org.example.waterattractionsrental.service.AttractionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.example.waterattractionsrental.dto.UserDTO;
import org.example.waterattractionsrental.dto.AttractionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

    @Operation(summary = "Pobierz wszystkie atrakcje", description = "Zwraca listę wszystkich atrakcji.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista atrakcji została zwrócona poprawnie")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<AttractionDTO> getAllAttractions() {
        return attractionService.getAllAttractions();
    }

    @Operation(summary = "Usuń atrakcję", description = "Usuwa atrakcję po ID. Dostępne dla administratora.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atrakcja została usunięta"),
            @ApiResponse(responseCode = "404", description = "Atrakcja nie została znaleziona")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        attractionService.deleteById(id);
    }


    @Operation(summary = "Pobierz atrakcję po ID", description = "Zwraca szczegóły atrakcji na podstawie ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atrakcja została znaleziona"),
            @ApiResponse(responseCode = "404", description = "Atrakcja nie została znaleziona")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AttractionDTO> getById(@PathVariable Long id) {
        return attractionService.getAttractionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Dodaj atrakcję", description = "Tworzy nową atrakcję. Dostępne dla administratora.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atrakcja została utworzona"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Attraction> createAttraction(@RequestBody Attraction attraction) {
        return ResponseEntity.ok(attractionService.save(attraction));
    }

    @Operation(summary = "Zaktualizuj atrakcję", description = "Aktualizuje istniejącą atrakcję. Dostępne dla administratora.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atrakcja została zaktualizowana"),
            @ApiResponse(responseCode = "404", description = "Atrakcja nie została znaleziona")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Attraction> updateAttraction(@PathVariable Long id, @RequestBody AttractionDTO dto) {
        Attraction updated = attractionService.updateAttraction(id, dto);
        return ResponseEntity.ok(updated);
    }
}
