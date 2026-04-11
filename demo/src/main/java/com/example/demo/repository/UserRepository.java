package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    // Spring Boot magically writes the SQL for this based on the name!
    AppUser findByEmail(String email); 
    java.util.List<AppUser> findByRole(String role);
}