package com.example.HealthEasy.repository;

import com.example.HealthEasy.entity.Doctor;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(@NotBlank(message = "Email is required.") String email);
}
