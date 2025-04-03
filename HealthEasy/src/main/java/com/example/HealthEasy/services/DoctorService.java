package com.example.HealthEasy.services;

import com.example.HealthEasy.entity.Doctor;
import com.example.HealthEasy.entity.DoctorApplication;
import com.example.HealthEasy.entity.User;
import com.example.HealthEasy.enums.ApplicationStatus;
import com.example.HealthEasy.enums.Role;
import com.example.HealthEasy.repository.DoctorApplicationRepository;
import com.example.HealthEasy.repository.DoctorRepository;
import com.example.HealthEasy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorApplicationRepository doctorApplicationRepository;
    private final NotificationService notificationService;

    public DoctorService(UserRepository userRepository, DoctorRepository doctorRepository, DoctorApplicationRepository doctorApplicationRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.doctorApplicationRepository = doctorApplicationRepository;
        this.notificationService = notificationService;
    }

    public Doctor approveDoctorApplication(Long appId){
        Optional<DoctorApplication> applicationOptional = doctorApplicationRepository.findById(appId);
        if (applicationOptional.isEmpty()) {
            throw new RuntimeException("Application not found.");
        }
        DoctorApplication application = applicationOptional.get();

        if (application.getStatus() == ApplicationStatus.APPROVED) {
            throw new RuntimeException("Application is already approved.");
        }

        application.setStatus(ApplicationStatus.APPROVED);
        doctorApplicationRepository.save(application);

        User user = application.getUser();
        user.setRole(Role.DOCTOR);
        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setName(application.getFullName());
        doctor.setSpeciality(application.getSpeciality());
        doctor.setHospital(application.getHospital());
        doctor.setLicenseNumber(application.getMedicalLicense());
        doctor.setCertifications(application.getCertifications());
        doctor.setExperienceYears(application.getExperienceYears());
        doctor.setEmail(application.getUser().getEmail());  // Set the email
        doctor.setPhoneNumber(application.getUser().getPhoneNumber());
        doctorRepository.save(doctor);

        notificationService.sendNotificationToUser(user,
                "Your doctor application has been approved. Welcome to the system!");

        // Notify the admin
        User admin = userRepository.findByRole(Role.ADMIN);
        if (admin != null) {
            notificationService.sendNotificationToUser(admin,
                    "You have approved " + user.getEmail() + " as a doctor.");
        }

        return doctor;
    }

    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id){
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found."));
    }

    public Doctor updateDoctor(Long doctorId, Doctor updatedDoctor){
        return doctorRepository.findById(doctorId)
                .map(existingDoctor -> {
                    existingDoctor.setName(updatedDoctor.getName());
                    existingDoctor.setSpeciality(updatedDoctor.getSpeciality());
                    existingDoctor.setEmail(updatedDoctor.getEmail());
                    existingDoctor.setPhoneNumber(updatedDoctor.getPhoneNumber());
                    existingDoctor.setHospital(updatedDoctor.getHospital());
                    existingDoctor.setLicenseNumber(updatedDoctor.getLicenseNumber());
                    existingDoctor.setCertifications(updatedDoctor.getCertifications());
                    existingDoctor.setExperienceYears(updatedDoctor.getExperienceYears());

                    return doctorRepository.save(existingDoctor);
                }).orElseThrow(() -> new RuntimeException("No doctor found."));
    }

    public void deleteDoctor(Long doctorId) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
        if (doctorOptional.isEmpty()) {
            throw new RuntimeException("Doctor not found.");
        }

        doctorRepository.deleteById(doctorId);
    }
}
