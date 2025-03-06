package com.example.HealthEasy.repository;

import com.example.HealthEasy.entity.Patient;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(@NotBlank(message = "Email not provided") String email);
}
