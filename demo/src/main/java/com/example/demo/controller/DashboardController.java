package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Assessment;
import com.example.demo.model.StudentGrade;
import com.example.demo.repository.AssessmentRepository;
import com.example.demo.repository.GradeRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private AssessmentRepository assessmentRepo;
    
    @Autowired
    private GradeRepository gradeRepo;

    // --- ASSESSMENTS API ---
    @GetMapping("/assessments")
    public List<Assessment> getAllAssessments() {
        return assessmentRepo.findAll();
    }

    @PostMapping("/assessments")
    public Assessment createAssessment(@RequestBody Assessment assessment) {
        return assessmentRepo.save(assessment);
    }

    // --- GRADES API ---
    @GetMapping("/grades")
    public List<StudentGrade> getAllGrades() {
        return gradeRepo.findAll();
    }

    @PostMapping("/grades")
    public StudentGrade createGrade(@RequestBody StudentGrade grade) {
        return gradeRepo.save(grade);
    }
    @DeleteMapping("/assessments/{id}")
    public ResponseEntity<?> deleteAssessment(@PathVariable Long id) {
        try {
            assessmentRepo.deleteById(id);
            return ResponseEntity.ok().body("{\"message\": \"Assessment deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Could not delete assessment\"}");
        }
    }
}