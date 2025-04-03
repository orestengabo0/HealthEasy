package com.example.HealthEasy.repository;

import com.example.HealthEasy.entity.User;
import com.example.HealthEasy.enums.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findByUsername(String username);
    Long countByRole(Role role);
    User findByRole(Role role);

    boolean existsByEmail(String email);
}
