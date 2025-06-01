package org.example.waterattractionsrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.waterattractionsrental.dto.ReservationDTO;
import org.example.waterattractionsrental.entity.Reservation;
import org.example.waterattractionsrental.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_shouldReturnList() throws Exception {
        ReservationDTO dto = ReservationDTO.builder()
                .id(1L)
                .attractionName("Kajak")
                .username("testuser")
                .build();

        Mockito.when(reservationService.getAllReservations()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].attractionName").value("Kajak"));
    }

    @Test
    void getReservationById_shouldReturnReservationIfExists() throws Exception {
        ReservationDTO dto = ReservationDTO.builder()
                .id(1L)
                .attractionName("Kajak")
                .username("testuser")
                .build();

        Mockito.when(reservationService.findDTOById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.attractionName").value("Kajak"));
    }

    @Test
    void getReservationById_shouldReturn404IfNotExists() throws Exception {
        Mockito.when(reservationService.findDTOById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservations/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createReservation_shouldReturnCreatedReservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(2L);

        ReservationDTO dto = ReservationDTO.builder()
                .id(2L)
                .attractionName("Kajak")
                .username("testuser")
                .build();

        Mockito.when(reservationService.saveAndReturnDTO(any(Reservation.class))).thenReturn(dto);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.attractionName").value("Kajak"));
    }

    @Test
    void delete_shouldCallService() throws Exception {
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isOk()); // jeśli masz @ResponseStatus(HttpStatus.NO_CONTENT), zmień na isNoContent()
        Mockito.verify(reservationService).deleteById(1L);
    }

    @Test
    void update_shouldReturnUpdatedReservation() throws Exception {
        ReservationDTO dto = ReservationDTO.builder()
                .attractionName("Kajak Update")
                .username("nowyuser")
                .build();

        Reservation updated = new Reservation();
        updated.setId(1L);

        Mockito.when(reservationService.updateReservation(eq(1L), any(ReservationDTO.class))).thenReturn(updated);
        Mockito.when(reservationService.mapToDTO(updated)).thenReturn(dto);

        mockMvc.perform(put("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attractionName").value("Kajak Update"))
                .andExpect(jsonPath("$.username").value("nowyuser"));
    }
}
