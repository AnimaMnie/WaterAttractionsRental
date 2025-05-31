package org.example.waterattractionsrental.service;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.dto.ReservationDTO;
import org.example.waterattractionsrental.entity.Reservation;
import org.example.waterattractionsrental.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReservationDTO> findDTOById(Long id) {
        return reservationRepository.findById(id)
                .map(this::mapToDTO);
    }

    public ReservationDTO saveAndReturnDTO(Reservation reservation) {
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

        Reservation saved = reservationRepository.save(reservation);
        return mapToDTO(saved);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    private ReservationDTO mapToDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .username(reservation.getUser().getUsername())
                .attractionName(reservation.getAttraction().getName())
                .attractionType(reservation.getAttraction().getType().name())
                .build();
    }
}
