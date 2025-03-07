package com.example.HealthEasy.repository;

import com.example.HealthEasy.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findById(Long id);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByEmailAndRoleIsNotDoctor(String userEmail);
}
