package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@RestController
@CrossOrigin(origins = "*") // Still allowing React to talk to us!
@RequestMapping("/api") // This puts "/api" in front of all routes in this file
public class StudentController {

    @Autowired // This tells Spring to automatically plug in the database translator
    private StudentRepository studentRepository;

    // 1. Your original status check! (http://localhost:8081/api/status)
    @GetMapping("/status")
    public String checkStatus() {
        return "The EduTracker Spring Boot Backend is LIVE and connected to MySQL! 🚀";
    }

    // 2. Fetch ALL students from the database (http://localhost:8081/api/students)
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll(); // Automatically runs "SELECT * FROM student"
    }

    // 3. Save a NEW student to the database
    @PostMapping("/students")
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student); // Automatically runs "INSERT INTO student..."
    }
}