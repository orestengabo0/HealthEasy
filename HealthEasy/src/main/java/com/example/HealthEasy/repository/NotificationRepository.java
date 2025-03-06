package com.example.HealthEasy.repository;

import com.example.HealthEasy.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDoctorIdAndIsReadFalse(Long doctorId);
    void deleteNotificationsByDoctorId(Long id);
}
