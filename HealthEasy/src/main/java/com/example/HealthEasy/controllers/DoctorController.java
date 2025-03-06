package com.example.HealthEasy.controllers;

import com.example.HealthEasy.entity.Doctor;
import com.example.HealthEasy.enums.Role;
import com.example.HealthEasy.repository.DoctorRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
    }
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR')")
    public Optional<Doctor> getDoctor(@PathVariable Long id){
        return doctorRepository.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Doctor createDoctor( @RequestBody Doctor doctor){
        doctor.setRole(Role.DOCTOR);
        return doctorRepository.save(doctor);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @Valid @RequestBody Doctor updatedDoctor){
        return doctorRepository.findById(id)
                .map(existingDoctor -> {
                    existingDoctor.setName(updatedDoctor.getName());
                    existingDoctor.setSpeciality(updatedDoctor.getSpeciality());

                    Doctor savedDoctor = doctorRepository.save(existingDoctor);
                    return ResponseEntity.ok(savedDoctor);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDoctor(@PathVariable Long id){
        doctorRepository.deleteById(id);
    }
}
