package com.example.HealthEasy.controllers;

import com.example.HealthEasy.entity.DoctorApplication;
import com.example.HealthEasy.entity.User;
import com.example.HealthEasy.enums.ApplicationStatus;
import com.example.HealthEasy.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/application")
public class DoctorApplicationController {
    private final UserRepository userRepository;

    public DoctorApplicationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/apply")
    public ResponseEntity<String> applyForDoctor(@RequestBody DoctorApplication application,
                                                 Authentication authentication){
        String userEmail = authentication.getName();
        Optional<User> existingUser = userRepository.findUserByEmailAndRoleIsNotDoctor(userEmail);
        if (existingUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        application.setUser(existingUser.get());
        application.setStatus(ApplicationStatus.PENDING);
        return ResponseEntity.ok("Your application has been submitted for review.");
    }
}
