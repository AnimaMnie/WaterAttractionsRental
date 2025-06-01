package org.example.waterattractionsrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.waterattractionsrental.dto.UserDTO;
import org.example.waterattractionsrental.entity.User;
import org.example.waterattractionsrental.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        UserDTO dto = UserDTO.builder()
                .id(1L)
                .username("admin")
                .role("ADMIN")
                .build();

        Mockito.when(userService.getAllUsers()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("admin"));
    }

    @Test
    void getUserById_shouldReturnUserIfExists() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Mockito.when(userService.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void getUserById_shouldReturn404IfNotExists() throws Exception {
        Mockito.when(userService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        User user = new User();
        user.setId(2L);
        user.setUsername("nowyuzytkownik");

        Mockito.when(userService.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.username").value("nowyuzytkownik"));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        UserDTO dto = UserDTO.builder()
                .username("updateduser")
                .role("ADMIN")
                .build();

        User updated = new User();
        updated.setId(1L);
        updated.setUsername("updateduser");

        Mockito.when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"));
    }

    @Test
    void deleteUser_shouldCallService() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk()); // jeśli masz 204 No Content w kontrolerze, zmień na .isNoContent()
        Mockito.verify(userService).deleteById(1L);
    }
}
