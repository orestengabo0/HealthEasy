package com.example.HealthEasy.repository;

import com.example.HealthEasy.entity.DoctorApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorApplicationRepository extends JpaRepository<DoctorApplication, Long> {
    Optional<DoctorApplication> findUserId(Long userId);
    List<DoctorApplication> getPendingApplications();

    Optional<DoctorApplication> findByUserId(Long userId);
}
