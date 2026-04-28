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
import com.example.demo.model.McqQuestion;
import com.example.demo.model.StudentGrade;
import com.example.demo.repository.AssessmentRepository;
import com.example.demo.repository.GradeRepository;
import com.example.demo.repository.McqQuestionRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private AssessmentRepository assessmentRepo;

    @Autowired
    private GradeRepository gradeRepo;

    @Autowired
    private McqQuestionRepository mcqRepo;

    // --- ASSESSMENTS API ---

    // Get ALL assessments (used only as fallback)
    @GetMapping("/assessments")
    public List<Assessment> getAllAssessments() {
        return assessmentRepo.findAll();
    }

    // Get assessments for a specific teacher
    @GetMapping("/assessments/teacher/{teacherId}")
    public List<Assessment> getAssessmentsByTeacher(@PathVariable Long teacherId) {
        return assessmentRepo.findByTeacherId(teacherId);
    }

    // Create assessment (now expects teacherId in the body)
    @PostMapping("/assessments")
    public Assessment createAssessment(@RequestBody Assessment assessment) {
        return assessmentRepo.save(assessment);
    }

    @DeleteMapping("/assessments/{id}")
    public ResponseEntity<?> deleteAssessment(@PathVariable Long id) {
        try {
            // Also delete all MCQ questions for this assessment
            List<McqQuestion> questions = mcqRepo.findByAssessmentId(id);
            mcqRepo.deleteAll(questions);
            assessmentRepo.deleteById(id);
            return ResponseEntity.ok().body("{\"message\": \"Assessment deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Could not delete assessment\"}");
        }
    }

    // --- MCQ QUESTIONS API ---

    // Get all questions for an assessment
    @GetMapping("/assessments/{assessmentId}/questions")
    public List<McqQuestion> getQuestions(@PathVariable Long assessmentId) {
        return mcqRepo.findByAssessmentId(assessmentId);
    }

    // Save a list of questions for an assessment
    @PostMapping("/assessments/{assessmentId}/questions")
    public List<McqQuestion> saveQuestions(
            @PathVariable Long assessmentId,
            @RequestBody List<McqQuestion> questions) {
        // Delete old questions first (idempotent save)
        List<McqQuestion> existing = mcqRepo.findByAssessmentId(assessmentId);
        mcqRepo.deleteAll(existing);

        for (McqQuestion q : questions) {
            q.setAssessmentId(assessmentId);
        }
        return mcqRepo.saveAll(questions);
    }

    // --- GRADES API ---

    @GetMapping("/grades")
    public List<StudentGrade> getAllGrades() {
        return gradeRepo.findAll();
    }

    // Get grades for a specific student
    @GetMapping("/grades/student/{studentId}")
    public List<StudentGrade> getGradesByStudent(@PathVariable Long studentId) {
        return gradeRepo.findByStudentId(studentId);
    }

    // Check if a student already submitted a specific assessment
    @GetMapping("/grades/student/{studentId}/assessment/{assessmentId}")
    public ResponseEntity<?> getGradeForAssessment(
            @PathVariable Long studentId,
            @PathVariable Long assessmentId) {
        StudentGrade grade = gradeRepo.findByStudentIdAndAssessmentId(studentId, assessmentId);
        if (grade != null) {
            return ResponseEntity.ok(grade);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/grades")
    public StudentGrade createGrade(@RequestBody StudentGrade grade) {
        return gradeRepo.save(grade);
    }
}