package com.example.HealthEasy.services;

import com.example.HealthEasy.Dto.DoctorApplicationDto;
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
public class DoctorApplicationService {
    private final UserRepository userRepository;
    private final DoctorApplicationRepository doctorApplicationRepository;
    private final NotificationService notificationService;

    public DoctorApplicationService(UserRepository userRepository,
                                    DoctorApplicationRepository doctorApplicationRepository,
                                    NotificationService notificationService, DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.doctorApplicationRepository = doctorApplicationRepository;
        this.notificationService = notificationService;
    }

    public DoctorApplication submitApplication(DoctorApplicationDto applicationDto, Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found.");
        }
        User user = userOptional.get();
        if(doctorApplicationRepository.findByUser(user).isPresent()){
            throw new RuntimeException("You have already submitted an application.");
        }

        DoctorApplication application = new DoctorApplication();
        application.setUser(user);
        application.setFullName(applicationDto.getFullName());
        application.setMedicalLicense(applicationDto.getMedicalLicense());
        application.setSpeciality(applicationDto.getSpeciality());
        application.setHospital(applicationDto.getHospital());
        application.setExperienceYears(applicationDto.getExperienceYears());
        application.setCertifications(applicationDto.getCertifications());
        application.setStatus(ApplicationStatus.PENDING);
        doctorApplicationRepository.save(application);

        notificationService.sendNotificationToUser(user,
                "Your application was submitted successfully.");
        User admin = userRepository.findByRole(Role.ADMIN);
        if(admin != null){
            notificationService.sendNotificationToUser(admin,
                    "A new doctor application has been submitted by" + user.getEmail());
        }

        return application;
    }

    public DoctorApplication approveApplication(Long appId){
        Optional<DoctorApplication> applicationOptional =
                doctorApplicationRepository.findById(appId);
        if(applicationOptional.isEmpty()){
            throw new RuntimeException("Application not found.");
        }
        DoctorApplication application = applicationOptional.get();
        application.setStatus(ApplicationStatus.APPROVED);
        doctorApplicationRepository.save(application);

        User admin = userRepository.findByRole(Role.ADMIN);
        User user = application.getUser();
        notificationService.sendNotificationToUser(application.getUser(),
                "Congratulations! Your application was approved. Welcome aboard!");
        notificationService.sendNotificationToUser(admin, "You recently approved "+
                user.getEmail() + " to be a doctor.");

        return application;
    }

    public DoctorApplication rejectApplication(Long appId){
        Optional<DoctorApplication> applicationOptional =
                doctorApplicationRepository.findById(appId);
        if(applicationOptional.isEmpty()){
            throw new RuntimeException("Application not found.");
        }
        DoctorApplication application = applicationOptional.get();
        application.setStatus(ApplicationStatus.REJECTED);
        doctorApplicationRepository.save(application);

        User admin = userRepository.findByRole(Role.ADMIN);
        User user = application.getUser();
        notificationService.sendNotificationToUser(user,
                "Your doctor application was rejected.");
        notificationService.sendNotificationToUser(admin,
                "You recently rejected application for "+ user.getEmail() + "!");

        return application;
    }

    public List<DoctorApplication> getPendingApplication(){
        return doctorApplicationRepository.findByStatus("PENDING");
    }

    public DoctorApplication getApplicationByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return doctorApplicationRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No application found for this user."));
    }
}
