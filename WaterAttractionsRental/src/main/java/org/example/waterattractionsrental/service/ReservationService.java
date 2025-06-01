package org.example.waterattractionsrental.service;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.dto.ReservationDTO;
import org.example.waterattractionsrental.entity.Reservation;
import org.example.waterattractionsrental.repository.AttractionRepository;
import org.example.waterattractionsrental.repository.ReservationRepository;
import org.example.waterattractionsrental.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AttractionRepository attractionRepository;

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
        Long userId = reservation.getUser().getId();

        var attraction = attractionRepository.findById(attractionId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono atrakcji o id " + attractionId));
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika o id " + userId));

        LocalDateTime start = reservation.getStartTime();
        LocalDateTime end = reservation.getEndTime();

        boolean overlapping = reservationRepository
                .existsByAttractionIdAndStartTimeLessThanAndEndTimeGreaterThan(
                        attractionId, end, start
                );

        if (overlapping) {
            throw new IllegalArgumentException("Ta atrakcja jest już zarezerwowana w tym przedziale czasowym.");
        }


        reservation.setAttraction(attraction);
        reservation.setUser(user);


        Reservation saved = reservationRepository.save(reservation);
        return mapToDTO(saved);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public ReservationDTO mapToDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .username(reservation.getUser().getUsername())
                .attractionName(reservation.getAttraction().getName())
                .attractionId(reservation.getAttraction().getId())
                .userId(reservation.getUser().getId())
                .attractionType(reservation.getAttraction().getType().name())
                .build();
    }

    public Reservation updateReservation(Long id, ReservationDTO dto) {
        return reservationRepository.findById(id)
                .map(existing -> {
                    if (dto.getStartTime() != null) {
                        existing.setStartTime(dto.getStartTime());
                    }
                    if (dto.getEndTime() != null) {
                        existing.setEndTime(dto.getEndTime());
                    }
                    if (dto.getUserId() != null) {
                        userRepository.findById(dto.getUserId())
                                .ifPresent(existing::setUser);
                    }
                    if (dto.getAttractionId() != null) {
                        attractionRepository.findById(dto.getAttractionId())
                                .ifPresent(existing::setAttraction);
                    }
                    return reservationRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }
}
