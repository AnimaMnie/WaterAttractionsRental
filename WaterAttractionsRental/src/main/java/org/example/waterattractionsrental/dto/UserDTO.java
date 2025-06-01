package org.example.waterattractionsrental.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String role;
}
