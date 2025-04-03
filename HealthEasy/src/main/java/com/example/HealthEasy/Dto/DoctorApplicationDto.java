package com.example.HealthEasy.Dto;

import com.example.HealthEasy.enums.ApplicationStatus;
import lombok.Data;

@Data
public class DoctorApplicationDto {
    private String fullName;
    private String medicalLicense;
    private String speciality;
    private String hospital;
    private int experienceYears;
    private String certifications;
    private ApplicationStatus status;
}
