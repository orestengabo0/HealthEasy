package com.example.HealthEasy.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserDto {
    @NotNull(message = "Email is required.")
    @Email(message = "Email format is incorrect.")
    private String email;

    @NotNull(message = "Password is required.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters only.")
    private String password;

    @NotNull(message = "Full name is required")
    private String username;

    @NotNull(message = "Phone number is required.")
    @Size(min = 10, max = 12, message = "Phone number must be between 10 and 12 digits.")
    private String phoneNumber;
}
