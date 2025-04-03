package com.example.HealthEasy.controllers;

import com.example.HealthEasy.Dto.LoginUserDto;
import com.example.HealthEasy.Dto.RegisterUserDto;
import com.example.HealthEasy.entity.User;
import com.example.HealthEasy.enums.Role;
import com.example.HealthEasy.repository.UserRepository;
import com.example.HealthEasy.responses.LoginResponse;
import com.example.HealthEasy.services.AuthenticationService;
import com.example.HealthEasy.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authentication(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.accepted().body(loginResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/{userId}")
    public ResponseEntity<?> createAdmin(@PathVariable Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            return new ResponseEntity<>("User not found",HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();
        if(user.getRole() == Role.ADMIN){
            return new ResponseEntity<>("User is already an admin user.", HttpStatus.BAD_REQUEST);
        }
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }
}
