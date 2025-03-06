package com.example.HealthEasy.entity;

import com.example.HealthEasy.enums.Gender;
import com.example.HealthEasy.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty("name")
    @NotBlank(message = "Provide your name.")
    private String name;

    @Column(nullable = false, unique = true)
    @JsonProperty("email")
    @NotBlank(message = "Email not provided")
    private String email;

    @Column(nullable = false)
    @JsonProperty("gender")
    @NotNull(message = "Choose gender between MALE, and FEMALE")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
