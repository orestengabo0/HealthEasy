package com.example.HealthEasy.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//@Configuration
public class StartupConfig {
//    @Bean
//    public ApplicationListener<ApplicationReadyEvent>
//    applicationReadyEventApplicationListener(UserDetailsService userDetailsService){
//        return event -> {
//            if( userDetailsService instanceof InMemoryUserDetailsManager){
//                InMemoryUserDetailsManager manager = (InMemoryUserDetailsManager) userDetailsService;
//                UserDetails user = manager.loadUserByUsername("user");
//                if(user!=null){
//                    String username = user.getUsername();
//                    String password = user.getPassword();
//                    System.out.println("========================================");
//                    System.out.println("Application Started Successfully!");
//                    System.out.println("Login Credentials:");
//                    System.out.println("Username: " + username);
//                    System.out.println("Password: " + password);
//                    System.out.println("========================================");
//                }
//            }
//        };
//    }
}
