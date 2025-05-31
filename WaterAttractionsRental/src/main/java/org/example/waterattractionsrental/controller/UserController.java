package org.example.waterattractionsrental.controller;

import lombok.RequiredArgsConstructor;
import org.example.waterattractionsrental.entity.User;
import org.example.waterattractionsrental.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.waterattractionsrental.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Users", description = "Operations related to application users")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "List all users", description = "Zwraca listę wszystkich użytkowników.")
    @ApiResponse(responseCode = "200", description = "Lista użytkowników")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }


    @Operation(summary = "Delete user", description = "Usuwa użytkownika z systemu.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Użytkownik usunięty"),
            @ApiResponse(responseCode = "404", description = "Użytkownik nie znaleziony")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }


    @Operation(summary = "Get user by ID", description = "Zwraca dane użytkownika na podstawie jego ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Użytkownik znaleziony"),
            @ApiResponse(responseCode = "404", description = "Użytkownik nie znaleziony")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Create a new user", description = "Tworzy nowego użytkownika.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Użytkownik utworzony"),
            @ApiResponse(responseCode = "400", description = "Błędne dane wejściowe")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }


    @Operation(summary = "Update user", description = "Aktualizuje dane użytkownika na podstawie ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Użytkownik zaktualizowany"),
            @ApiResponse(responseCode = "404", description = "Użytkownik nie znaleziony")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        User updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

}
