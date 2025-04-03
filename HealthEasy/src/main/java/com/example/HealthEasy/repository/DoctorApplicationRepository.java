package com.example.HealthEasy.repository;

import com.example.HealthEasy.entity.DoctorApplication;
import com.example.HealthEasy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorApplicationRepository extends JpaRepository<DoctorApplication, Long> {
    List<DoctorApplication> findByStatus(String status);
    Optional<DoctorApplication> findByUser(User user);
}
