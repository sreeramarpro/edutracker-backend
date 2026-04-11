package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.JoinRequest;
import com.example.demo.repository.JoinRequestRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/requests")
public class JoinRequestController {

    @Autowired
    private JoinRequestRepository requestRepository;

    // 1. Student creates a new request
    @PostMapping("/create")
    public JoinRequest createRequest(@RequestBody JoinRequest request) {
        return requestRepository.save(request);
    }

    // 2. Teacher fetches their pending lobby
    @GetMapping("/pending/{teacherId}")
    public List<JoinRequest> getPendingRequests(@PathVariable Long teacherId) {
        return requestRepository.findByTeacherIdAndStatus(teacherId, "PENDING");
    }

    // 3. Teacher approves a request
    @PutMapping("/{requestId}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long requestId) {
        JoinRequest request = requestRepository.findById(requestId).orElse(null);
        if (request != null) {
            request.setStatus("APPROVED");
            requestRepository.save(request);
            return ResponseEntity.ok(request);
        }
        return ResponseEntity.badRequest().body("Request not found");
    }
    // 4. Fetch the teacher's official approved roster
    @GetMapping("/approved/{teacherId}")
    public List<JoinRequest> getApprovedRequests(@PathVariable Long teacherId) {
        return requestRepository.findByTeacherIdAndStatus(teacherId, "APPROVED");
    }

    // 5. For the "Seed" and "Add Student" buttons (Auto-Approves them!)
    @PostMapping("/direct-add")
    public JoinRequest directAddStudent(@RequestBody JoinRequest request) {
        request.setStatus("APPROVED");
        return requestRepository.save(request);
    }
}