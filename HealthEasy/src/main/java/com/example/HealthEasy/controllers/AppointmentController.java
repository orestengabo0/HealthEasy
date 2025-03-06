package com.example.HealthEasy.controllers;

import com.example.HealthEasy.entity.Appointment;
import com.example.HealthEasy.entity.Doctor;
import com.example.HealthEasy.entity.Patient;
import com.example.HealthEasy.enums.AppointmentStatus;
import com.example.HealthEasy.repository.AppointmentRepository;
import com.example.HealthEasy.repository.DoctorRepository;
import com.example.HealthEasy.repository.PatientRepository;
import com.example.HealthEasy.services.AuthenticationService;
import com.example.HealthEasy.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final NotificationService notificationService;

    public AppointmentController(AppointmentRepository appointmentRepository,
                                 DoctorRepository doctorRepository,
                                 PatientRepository patientRepository,
                                 NotificationService notificationService){
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping
    public List<Appointment> getAllAppointments(){
        return appointmentRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping("/{id}")
    public Optional<Appointment> getAppointment(@PathVariable Long id){
        return appointmentRepository.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping
    public ResponseEntity<?> createAppointment(@Valid @RequestBody Appointment appointment, Authentication authentication){
        Optional<Doctor> existingDoctor = doctorRepository.findById(appointment.getDoctor().getId());
        if(existingDoctor.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor with id: "+
                    appointment.getDoctor().getId()+ " doesn't exist.");
        }
        String loggedInPatient = authentication.getName();
        Optional<Patient> existingPatient = patientRepository.findByEmail(loggedInPatient);
        if(existingPatient.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id: "+
                    appointment.getPatient().getId()+" doesn't exist.");
        }

        boolean hasActiveAppointment =
                appointmentRepository.existsByPatientIdAndAppointmentStatusNotIn(appointment.getPatient().getId(),
                        List.of(AppointmentStatus.CANCELED, AppointmentStatus.COMPLETED));

        if(hasActiveAppointment){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body("You already have an incomplete appointment.");
        }

        boolean isDoctorAvailable = appointmentRepository.existsByDoctorIdAndDateTimeAndAppointmentStatusNotIn(
                appointment.getDoctor().getId(),
                appointment.getDateTime(),
                List.of(AppointmentStatus.CANCELED, AppointmentStatus.COMPLETED)
        );

        if(!isDoctorAvailable){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The doctor is not available at this time.");
        }

        appointment.setDoctor(existingDoctor.get());
        appointment.setPatient(existingPatient.get());

        notificationService.sendNotification(appointment.getDoctor().getId(),
                "New appointment scheduled with " +
                        appointment.getPatient().getName() + " on " + appointment.getDateTime());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id,
                                               @Valid @RequestBody Appointment appointment,
                                               Authentication authentication
                                               ){
        return appointmentRepository.findById(id)
                .map(existingAppointment -> {
                    if(existingAppointment.getAppointmentStatus() == AppointmentStatus.CANCELED ||
                            existingAppointment.getAppointmentStatus() == AppointmentStatus.COMPLETED){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                                body("Cannot update a canceled or completed appointment.");
                    }
                    Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctor().getId());
                    if(doctor.isEmpty()){
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
                    }
                    String loggedInPatient = authentication.getName();
                    existingAppointment.setDoctor(doctor.get());
                    existingAppointment.setDateTime(appointment.getDateTime());
                    appointmentRepository.save(existingAppointment);

                    notificationService.sendNotification(doctor.get().getId(),
                            loggedInPatient + " that recently booked an appointment has changed the date/time to " +
                                    appointment.getDateTime());

                    return ResponseEntity.ok(existingAppointment);
                }).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PatchMapping("/{id}/completed")
    public ResponseEntity<?> setAppointmentStatusAsCompleted(@PathVariable Long id){
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
        if(existingAppointment.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Appointment appointment = existingAppointment.get();
        if(appointment.getAppointmentStatus() == AppointmentStatus.COMPLETED){
            return ResponseEntity.badRequest().body("Appointment is already completed.");
        }
        appointment.setAppointmentAsCompleted();
        appointmentRepository.save(appointment);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(appointment);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id){
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
        if(existingAppointment.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Appointment appointment = existingAppointment.get();
        if(appointment.getAppointmentStatus() == AppointmentStatus.COMPLETED){
            return ResponseEntity.badRequest().body("Cannot cancel a completed appointment.");
        }
        appointment.cancelAppointment();
        appointmentRepository.save(appointment);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(existingAppointment);
    }
}