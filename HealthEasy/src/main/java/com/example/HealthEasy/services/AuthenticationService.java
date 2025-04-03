package com.example.HealthEasy.services;

import com.example.HealthEasy.Dto.LoginUserDto;
import com.example.HealthEasy.Dto.RegisterUserDto;
import com.example.HealthEasy.entity.User;
import com.example.HealthEasy.enums.Role;
import com.example.HealthEasy.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private Role role = Role.USER;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {

        User user = new User()
                .setUsername(input.getUsername())
                .setEmail(input.getEmail())
                .setPhoneNumber(input.getPhoneNumber())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setRole(role);

        return userRepository.save(user);
    }

    public User authentication(LoginUserDto input){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );

        return userRepository.findUserByEmail(input.getEmail()).orElseThrow();
    }
}
