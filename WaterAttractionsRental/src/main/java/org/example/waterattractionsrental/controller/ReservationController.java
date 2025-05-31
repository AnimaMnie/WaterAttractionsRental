package org.example.waterattractionsrental.controller;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.dto.ReservationDTO;
import org.example.waterattractionsrental.entity.Reservation;
import org.example.waterattractionsrental.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ReservationDTO> getAll() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return reservationService.findDTOById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.saveAndReturnDTO(reservation));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        reservationService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> update(@PathVariable Long id, @RequestBody ReservationDTO dto) {
        Reservation updated = reservationService.updateReservation(id, dto);
        return ResponseEntity.ok(reservationService.mapToDTO(updated));
    }
}
