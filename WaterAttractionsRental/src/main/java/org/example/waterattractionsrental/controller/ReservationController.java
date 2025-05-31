package org.example.waterattractionsrental.controller;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.dto.ReservationDTO;
import org.example.waterattractionsrental.entity.Reservation;
import org.example.waterattractionsrental.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "Pobierz wszystkie rezerwacje", description = "Zwraca listę wszystkich rezerwacji. Dostępne tylko dla administratora.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista rezerwacji została zwrócona poprawnie"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReservationDTO> getAll() {
        return reservationService.getAllReservations();
    }

    @Operation(summary = "Pobierz rezerwację po ID", description = "Zwraca pojedynczą rezerwację na podstawie identyfikatora.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rezerwacja została znaleziona"),
            @ApiResponse(responseCode = "404", description = "Rezerwacja nie została znaleziona"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return reservationService.findDTOById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Utwórz rezerwację", description = "Tworzy nową rezerwację. Dostępne dla użytkownika.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rezerwacja została utworzona"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe"),
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.saveAndReturnDTO(reservation));
    }

    @Operation(summary = "Usuń rezerwację", description = "Usuwa rezerwację na podstawie ID. Dostępne dla administratora.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Rezerwacja została usunięta"),
            @ApiResponse(responseCode = "404", description = "Rezerwacja nie została znaleziona"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        reservationService.deleteById(id);
    }

    @Operation(summary = "Zaktualizuj rezerwację", description = "Aktualizuje istniejącą rezerwację. Dostępne dla administratora.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rezerwacja została zaktualizowana"),
            @ApiResponse(responseCode = "404", description = "Rezerwacja nie została znaleziona"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> update(@PathVariable Long id, @RequestBody ReservationDTO dto) {
        Reservation updated = reservationService.updateReservation(id, dto);
        return ResponseEntity.ok(reservationService.mapToDTO(updated));
    }
}
