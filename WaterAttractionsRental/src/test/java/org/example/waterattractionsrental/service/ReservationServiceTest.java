package org.example.waterattractionsrental.service;

import org.example.waterattractionsrental.dto.ReservationDTO;
import org.example.waterattractionsrental.entity.Attraction;
import org.example.waterattractionsrental.entity.Reservation;
import org.example.waterattractionsrental.entity.User;
import org.example.waterattractionsrental.entity.AttractionType;
import org.example.waterattractionsrental.repository.AttractionRepository;
import org.example.waterattractionsrental.repository.ReservationRepository;
import org.example.waterattractionsrental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AttractionRepository attractionRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private User user;
    private Attraction attraction;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        attraction = new Attraction();
        attraction.setId(2L);
        attraction.setName("Test Attraction");
        attraction.setType(AttractionType.KAJAK);

        reservation = new Reservation();
        reservation.setId(3L);
        reservation.setUser(user);
        reservation.setAttraction(attraction);
        reservation.setStartTime(LocalDateTime.of(2025, 6, 1, 10, 0));
        reservation.setEndTime(LocalDateTime.of(2025, 6, 1, 11, 0));
    }

    @Test
    void getAllReservations_shouldReturnDTOs() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        List<ReservationDTO> result = reservationService.getAllReservations();
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        verify(reservationRepository).findAll();
    }

    @Test
    void findDTOById_shouldReturnDTOIfFound() {
        when(reservationRepository.findById(3L)).thenReturn(Optional.of(reservation));
        Optional<ReservationDTO> result = reservationService.findDTOById(3L);
        assertTrue(result.isPresent());
        assertEquals("Test Attraction", result.get().getAttractionName());
    }

    @Test
    void findDTOById_shouldReturnEmptyIfNotFound() {
        when(reservationRepository.findById(100L)).thenReturn(Optional.empty());
        Optional<ReservationDTO> result = reservationService.findDTOById(100L);
        assertTrue(result.isEmpty());
    }

    @Test
    void saveAndReturnDTO_shouldSaveIfNoOverlap() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(attractionRepository.findById(2L)).thenReturn(Optional.of(attraction));
        when(reservationRepository.existsByAttractionIdAndStartTimeLessThanAndEndTimeGreaterThan(
                2L, reservation.getEndTime(), reservation.getStartTime()))
                .thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationDTO dto = reservationService.saveAndReturnDTO(reservation);
        assertEquals(reservation.getId(), dto.getId());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void saveAndReturnDTO_shouldThrowIfOverlap() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(attractionRepository.findById(2L)).thenReturn(Optional.of(attraction));
        when(reservationRepository.existsByAttractionIdAndStartTimeLessThanAndEndTimeGreaterThan(
                2L, reservation.getEndTime(), reservation.getStartTime()))
                .thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reservationService.saveAndReturnDTO(reservation));
        assertTrue(ex.getMessage().contains("już zarezerwowana"));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void deleteById_shouldCallRepository() {
        reservationService.deleteById(3L);
        verify(reservationRepository).deleteById(3L);
    }

    @Test
    void mapToDTO_shouldMapAllFields() {
        ReservationDTO dto = reservationService.mapToDTO(reservation);
        assertEquals(reservation.getId(), dto.getId());
        assertEquals(reservation.getAttraction().getId(), dto.getAttractionId());
        assertEquals(reservation.getUser().getId(), dto.getUserId());
        assertEquals(reservation.getStartTime(), dto.getStartTime());
        assertEquals(reservation.getEndTime(), dto.getEndTime());
        assertEquals(reservation.getAttraction().getName(), dto.getAttractionName());
        assertEquals(reservation.getUser().getUsername(), dto.getUsername());
        assertEquals(reservation.getAttraction().getType().name(), dto.getAttractionType());
    }

    @Test
    void updateReservation_shouldUpdateFields() {
        ReservationDTO dto = ReservationDTO.builder()
                .startTime(LocalDateTime.of(2025, 6, 1, 12, 0))
                .endTime(LocalDateTime.of(2025, 6, 1, 13, 0))
                .userId(1L)
                .attractionId(2L)
                .build();

        when(reservationRepository.findById(3L)).thenReturn(Optional.of(reservation));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(attractionRepository.findById(2L)).thenReturn(Optional.of(attraction));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Reservation updated = reservationService.updateReservation(3L, dto);

        assertEquals(dto.getStartTime(), updated.getStartTime());
        assertEquals(dto.getEndTime(), updated.getEndTime());
        assertEquals(user, updated.getUser());
        assertEquals(attraction, updated.getAttraction());
    }

    @Test
    void updateReservation_shouldThrowIfNotFound() {
        ReservationDTO dto = ReservationDTO.builder().build();
        when(reservationRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reservationService.updateReservation(100L, dto));
    }
}
