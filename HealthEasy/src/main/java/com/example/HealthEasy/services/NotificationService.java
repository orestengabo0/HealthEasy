package com.example.HealthEasy.services;

import com.example.HealthEasy.entity.Doctor;
import com.example.HealthEasy.entity.Notification;
import com.example.HealthEasy.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    public void sendNotification(Long doctorId, String message){
        Notification notification = new Notification();
        notification.setDoctor(new Doctor(doctorId));
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications(Long doctorId){
        return notificationRepository.findByDoctorIdAndIsReadFalse(doctorId);
    }
}
