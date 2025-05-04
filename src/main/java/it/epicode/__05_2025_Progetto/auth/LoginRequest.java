package it.epicode.__05_2025_Progetto.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
