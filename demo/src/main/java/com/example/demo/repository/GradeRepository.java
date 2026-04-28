package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.StudentGrade;

@Repository
public interface GradeRepository extends JpaRepository<StudentGrade, Long> {
    List<StudentGrade> findByStudentId(Long studentId);
    StudentGrade findByStudentIdAndAssessmentId(Long studentId, Long assessmentId);
}