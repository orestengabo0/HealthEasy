package com.example.HealthEasy.services;

import com.example.HealthEasy.Dto.DoctorDto;
import com.example.HealthEasy.entity.Doctor;
import com.example.HealthEasy.entity.User;
import com.example.HealthEasy.enums.Role;
import com.example.HealthEasy.repository.DoctorRepository;
import com.example.HealthEasy.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public DoctorService(UserRepository userRepository, DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    public ResponseEntity<Doctor> registerDoctor(DoctorDto doctorDto, Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        if(doctorRepository.findByEmail(doctorDto.getEmail()).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        user.setRole(Role.DOCTOR);
        userRepository.save(user);

        Doctor doctor = getDoctor(doctorDto, user);

        doctorRepository.save(doctor);
        return ResponseEntity.ok(doctor);
    }

    private static Doctor getDoctor(DoctorDto doctorDto, User user) {
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setName(doctorDto.getName());
        doctor.setSpeciality(doctorDto.getSpeciality());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setPhoneNumber(doctorDto.getPhoneNumber());
        doctor.setHospital(doctorDto.getHospital());
        doctor.setLicenseNumber(doctorDto.getLicenseNumber());
        doctor.setCertifications(doctorDto.getCertifications());
        doctor.setExperienceYears(doctorDto.getExperienceYears());
        return doctor;
    }

    ;
}
