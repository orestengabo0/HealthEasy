package com.example.HealthEasy.controllers;

import com.example.HealthEasy.entity.Doctor;
import com.example.HealthEasy.entity.Notification;
import com.example.HealthEasy.repository.DoctorRepository;
import com.example.HealthEasy.repository.NotificationRepository;
import com.example.HealthEasy.services.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final DoctorRepository doctorRepository;

    public NotificationController(NotificationService notificationService,
                                  NotificationRepository notificationRepository,
                                  DoctorRepository doctorRepository) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.doctorRepository = doctorRepository;
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUnreadNotifications(@PathVariable Long id){
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if(doctor.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<Notification> notificationList = notificationService.getUnreadNotifications(id);
        if(notificationList.isEmpty()){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("No notification found.");
        }
        return ResponseEntity.ok(notificationList);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PutMapping("/{doctorId}/{notificationId}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long doctorId,
                                                    @PathVariable Long notificationId){
        Optional<Notification> notificationOpt =
                notificationRepository.findById(notificationId);

        if (notificationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Notification notification = notificationOpt.get();
        if (!notification.getDoctor().getId().equals(doctorId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not authorized to mark this notification as read.");
        }
        if (notification.isRead()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Notification is already marked as read.");
        }
        notification.setRead(true);
        notificationRepository.save(notification);

        return ResponseEntity.ok("Notification marked as read.");
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @DeleteMapping("/{doctorId}/{notificationId}/clear")
    public ResponseEntity<?> clearNotification(@PathVariable Long doctorId,
                                             @PathVariable Long notificationId){
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        if(doctor.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<Notification> notificationList = notificationService.
                getUnreadNotifications(doctorId);
        Optional<Notification> notificationToDelete = notificationList.stream()
                        .filter(n -> n.getId().equals(notificationId))
                                .findFirst();
        if (notificationToDelete.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("Notification not found for this doctor.");
        }
        notificationRepository.delete(notificationToDelete.get());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Notification cleared.");
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @DeleteMapping("/{doctorId}/clearall")
    public ResponseEntity<?> clearAllNotifications(@PathVariable Long doctorId){
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        if(doctor.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        notificationRepository.deleteNotificationsByDoctorId(doctorId);
        return ResponseEntity.ok("All notifications are cleared.");
    }
}
