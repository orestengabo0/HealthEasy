package com.example.HealthEasy.entity;

import com.example.HealthEasy.enums.ApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotNull
    @Column(nullable = false)
    private String fullName;  // Full legal name

    @NotNull
    @Column(nullable = false)
    private String medicalLicense; // License number

    @NotNull
    @Column(nullable = false)
    private String speciality;  // Doctor's area of expertise

    @NotNull
    @Column(nullable = false)
    private String hospital;  // Current or previous hospital

    @NotNull
    private int experienceYears; // Number of years of experience

    private String certifications; // Optional field for additional certifications

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now(); // Time of application submission
}
