package com.fit.demo.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank (message = "nombre is required")
    private String nombre;
    
    @Email @NotBlank (message = "email is required")
    private String email;
    
    @NotBlank (message = "password is required")
    private String password;



}
