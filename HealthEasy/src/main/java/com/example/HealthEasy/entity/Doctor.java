package com.example.HealthEasy.entity;

import com.example.HealthEasy.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty("name")
    @NotBlank(message = "Name is required.")
    private String name;

    @Column(nullable = false)
    @JsonProperty("speciality")
    @NotBlank(message = "Doctor's speciality is missing.")
    private String speciality;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Doctor(Long id){
        this.id = id;
    }
}
