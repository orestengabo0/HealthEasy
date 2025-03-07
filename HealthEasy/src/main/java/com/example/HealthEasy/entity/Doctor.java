package com.example.HealthEasy.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty("name")
    @NotBlank(message = "Name is required.")
    private String name;

    @Column(nullable = false)
    @JsonProperty("speciality")
    @NotBlank(message = "Doctor's speciality is missing.")
    private String speciality;

    @Column(unique = true, nullable = false)
    @JsonProperty("email")
    @NotBlank(message = "Email is required.")
    private String email;

    @Column(nullable = false)
    @JsonProperty("phoneNumber")
    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;

    @Column(nullable = false)
    @JsonProperty("hospital")
    @NotBlank(message = "Hospital/clinic is required.")
    private String hospital;

    @Column(nullable = false)
    @JsonProperty("licenseNumber")
    @NotBlank(message = "Medical license number is required.")
    private String licenseNumber;

    @JsonProperty("certifications")
    private String certifications; // Optional field for additional certifications

    @JsonProperty("profileImage")
    private String profileImage; // URL/path for doctor's profile picture

    @Column(nullable = false)
    private int experienceYears; // Years of experience in practice

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private LocalDateTime createdAt = LocalDateTime.now(); // Date doctor profile was created

    public Doctor(Long doctorId) {
        this.id = doctorId;
    }
}
