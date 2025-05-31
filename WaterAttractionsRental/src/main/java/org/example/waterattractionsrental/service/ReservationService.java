package org.example.waterattractionsrental.service;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.entity.Reservation;
import org.example.waterattractionsrental.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation save(Reservation reservation) {
        Long attractionId = reservation.getAttraction().getId();
        LocalDateTime start = reservation.getStartTime();
        LocalDateTime end = reservation.getEndTime();

        boolean overlapping = reservationRepository
                .existsByAttractionIdAndStartTimeLessThanAndEndTimeGreaterThan(
                        attractionId, end, start
                );

        if (overlapping) {
            throw new IllegalArgumentException("Ta atrakcja jest już zarezerwowana w tym przedziale czasowym.");
        }

        return reservationRepository.save(reservation);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
