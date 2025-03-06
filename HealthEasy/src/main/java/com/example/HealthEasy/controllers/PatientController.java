package com.example.HealthEasy.controllers;

import com.example.HealthEasy.entity.Patient;
import com.example.HealthEasy.enums.Role;
import com.example.HealthEasy.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public Optional<Patient> getPatient(@PathVariable Long id){
        return patientRepository.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/me")
    public ResponseEntity<Patient> getCurrentPatient(Authentication authentication) {
        String loggedInEmail = authentication.getName(); // Get logged-in user's email
        Optional<Patient> patient = patientRepository.findByEmail(loggedInEmail);

        return patient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> createPatient(@Valid @RequestBody Patient patient){
        try{
            Optional<Patient> existingPatient = patientRepository.findByEmail(patient.getEmail());
            if(existingPatient.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: Email "+patient.getEmail()+" already exists.");
            }
            patient.setRole(Role.PATIENT);

            Patient savedPatient = patientRepository.save(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        }catch (DataIntegrityViolationException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + ex.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id,@Valid @RequestBody Patient updatedPatient) {
        return patientRepository.findById(id)
                .map(existingPatient -> {
                    Optional<Patient> patientWithSameEmail = patientRepository.findByEmail(updatedPatient.getEmail());
                    if(patientWithSameEmail.isPresent() && !patientWithSameEmail.get().getId().equals(id)){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Error: Email '" + updatedPatient.getEmail() + "' is already in use.");
                    }
                    existingPatient.setName(updatedPatient.getName());
                    existingPatient.setEmail(updatedPatient.getEmail());
                    existingPatient.setGender(updatedPatient.getGender());

                    Patient savedPatient = patientRepository.save(existingPatient);
                    return ResponseEntity.ok(savedPatient);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id){
        if(!patientRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        patientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
