package com.example.HealthEasy.controllers;

import com.example.HealthEasy.entity.Doctor;
import com.example.HealthEasy.services.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Doctor> getAllDoctors(){
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR')")
    public Doctor getDoctor(@PathVariable Long id){
        return doctorService.getDoctorById(id);
    }

    @PostMapping("/approve/{appId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Doctor> approveDoctor(@PathVariable Long appId){
        Doctor doctor = doctorService.approveDoctorApplication(appId);
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @Valid @RequestBody Doctor updatedDoctor){
        Doctor doctor = doctorService.updateDoctor(id, updatedDoctor);
        return ResponseEntity.ok(doctor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDoctor(@PathVariable Long id){
        doctorService.deleteDoctor(id);
    }
}
