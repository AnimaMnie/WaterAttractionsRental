package org.example.waterattractionsrental.service;

import org.example.waterattractionsrental.dto.UserDTO;
import org.example.waterattractionsrental.entity.Role;
import org.example.waterattractionsrental.entity.User;
import org.example.waterattractionsrental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("secret");
        user.setRole(Role.USER);
    }

    @Test
    void getAllUsers_shouldReturnListOfUserDTOs() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        verify(userRepository).findAll();
    }

    @Test
    void save_shouldPersistUser() {
        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.save(user);

        assertNotNull(saved);
        assertEquals("testuser", saved.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void findById_shouldReturnUserIfExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void findByUsername_shouldReturnUserIfExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void deleteById_shouldCallRepository() {
        userService.deleteById(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void updateUser_shouldUpdateFieldsAndReturnUser() {
        UserDTO dto = UserDTO.builder()
                .username("updateduser")
                .password("newpass")
                .role("ADMIN")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("hashed");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User updated = userService.updateUser(1L, dto);

        assertEquals("updateduser", updated.getUsername());
        assertEquals("hashed", updated.getPassword());
        assertEquals(Role.ADMIN, updated.getRole());
    }

    @Test
    void updateUser_shouldThrowExceptionIfNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        UserDTO dto = UserDTO.builder().build();

        assertThrows(RuntimeException.class, () -> userService.updateUser(99L, dto));
    }
}
