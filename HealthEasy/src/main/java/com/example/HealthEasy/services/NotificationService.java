package com.example.HealthEasy.services;

import com.example.HealthEasy.entity.Notification;
import com.example.HealthEasy.entity.User;
import com.example.HealthEasy.enums.NotificationType;
import com.example.HealthEasy.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    public void sendNotificationToUser(User user, String message){
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(NotificationType.USER_SPECIFIC);
        notificationRepository.save(notification);
    }

    public void sendSystemNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setType(NotificationType.SYSTEM);
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForUser(User user){
        return notificationRepository.findByUser(user);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
}
