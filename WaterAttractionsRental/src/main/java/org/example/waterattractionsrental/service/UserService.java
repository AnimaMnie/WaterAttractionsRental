package org.example.waterattractionsrental.service;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.entity.Role;
import org.example.waterattractionsrental.entity.User;
import org.example.waterattractionsrental.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.example.waterattractionsrental.dto.UserDTO;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .role(user.getRole().name())
                        .build())
                .toList();
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, UserDTO dto) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setUsername(dto.getUsername());

                    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                        existing.setPassword(passwordEncoder.encode(dto.getPassword()));
                    }

                    if (dto.getRole() != null && !dto.getRole().isBlank()) {
                        existing.setRole(Role.valueOf(dto.getRole().toUpperCase()));
                    }

                    return userRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
