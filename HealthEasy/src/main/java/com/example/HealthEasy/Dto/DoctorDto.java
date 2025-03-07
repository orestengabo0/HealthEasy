package com.example.HealthEasy.Dto;

import lombok.Data;

@Data
public class DoctorDto {
    private String name;
    private String speciality;
    private String email;
    private String phoneNumber;
    private String hospital;
    private String licenseNumber;
    private String certifications;
    private int experienceYears;
}
