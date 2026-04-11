package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.JoinRequest;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {
    // Finds all requests waiting for a specific teacher
    List<JoinRequest> findByTeacherIdAndStatus(Long teacherId, String status);
    
    // Checks if a student already sent a request
    JoinRequest findByStudentId(Long studentId);
}