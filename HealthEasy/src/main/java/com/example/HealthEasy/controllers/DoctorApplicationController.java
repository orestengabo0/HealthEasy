package com.example.HealthEasy.controllers;

import com.example.HealthEasy.Dto.DoctorApplicationDto;
import com.example.HealthEasy.entity.DoctorApplication;
import com.example.HealthEasy.entity.User;
import com.example.HealthEasy.services.DoctorApplicationService;
import com.example.HealthEasy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/application")
public class DoctorApplicationController {
    @Autowired
    private DoctorApplicationService doctorApplicationService;
    @Autowired
    private UserService userService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitApplication(@RequestBody DoctorApplicationDto
                                                           applicationDto){
        try {
            Authentication authentication = SecurityContextHolder.
                    getContext().getAuthentication();
            String username = authentication.getName();
            Long userId = userService.getUserIdByUsername(username);
            DoctorApplication application = doctorApplicationService.
                    submitApplication(applicationDto, userId);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{appId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> approveApplication(@PathVariable Long appId){
        try{
            DoctorApplication application = doctorApplicationService.approveApplication(appId);
            return ResponseEntity.ok(application);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reject/{appId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> rejectApplication(@PathVariable Long appId){
        try{
            DoctorApplication application = doctorApplicationService.rejectApplication(appId);
            return ResponseEntity.ok(application);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getPendingApplications(){
        List<DoctorApplication> pendingApplications = doctorApplicationService.getPendingApplication();
        return ResponseEntity.ok(pendingApplications);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getApplicationByUser(@PathVariable Long userId){
        try {
            DoctorApplication application = doctorApplicationService.getApplicationByUserId(userId);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
