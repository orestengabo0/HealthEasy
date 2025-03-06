package com.example.HealthEasy.entity;

import com.example.HealthEasy.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    @Future(message = "Appointment date must be in the future.")
    private LocalDateTime dateTime;

    private LocalDateTime canceledAt;
    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus = AppointmentStatus.SCHEDULED;

    @PrePersist
    protected void onCreate(){
        if(appointmentStatus == null){
            appointmentStatus = AppointmentStatus.SCHEDULED;
        }
    }

    public void cancelAppointment(){
        this.appointmentStatus = AppointmentStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }

    public void setAppointmentAsCompleted(){
        this.appointmentStatus = AppointmentStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
}
