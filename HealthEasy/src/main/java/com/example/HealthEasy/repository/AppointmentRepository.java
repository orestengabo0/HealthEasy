package com.example.HealthEasy.repository;

import com.example.HealthEasy.entity.Appointment;
import com.example.HealthEasy.enums.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByPatientIdAndAppointmentStatusNotIn
            (Long patientId,
             Collection<AppointmentStatus> appointmentStatuses);

    boolean existsByDoctorIdAndDateTimeAndAppointmentStatusNotIn
            (Long id,
             @NotNull @Future(message = "Appointment date must be in the future.") LocalDateTime dateTime,
             List<AppointmentStatus> canceled);
}
