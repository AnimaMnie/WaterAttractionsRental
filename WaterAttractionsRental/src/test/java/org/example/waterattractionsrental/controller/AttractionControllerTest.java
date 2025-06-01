package org.example.waterattractionsrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.waterattractionsrental.dto.AttractionDTO;
import org.example.waterattractionsrental.entity.AttractionType;
import org.example.waterattractionsrental.service.AttractionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AttractionController.class)

@WithMockUser(roles = "USER")
class AttractionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttractionService attractionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllAttractions_shouldReturnList() throws Exception {
        AttractionDTO dto = AttractionDTO.builder()
                .id(1L)
                .name("Kajak")
                .type(AttractionType.KAJAK)
                .price(120.0)
                .available(true)
                .build();

        Mockito.when(attractionService.getAllAttractions()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/attractions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Kajak"));
    }

    @Test
    void getById_shouldReturnAttractionIfExists() throws Exception {
        AttractionDTO dto = AttractionDTO.builder()
                .id(1L)
                .name("Kajak")
                .type(AttractionType.KAJAK)
                .price(120.0)
                .available(true)
                .build();

        Mockito.when(attractionService.getAttractionById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/attractions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Kajak"));
    }

    @Test
    void getById_shouldReturn404IfNotExists() throws Exception {
        Mockito.when(attractionService.getAttractionById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/attractions/999"))
                .andExpect(status().isNotFound());
    }
}
