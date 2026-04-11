package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AppUser;
import com.example.demo.model.JoinRequest;
import com.example.demo.repository.JoinRequestRepository;
import com.example.demo.repository.UserRepository;

@RestController
@CrossOrigin(origins = "*") // Allows React to connect safely
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // 🛡️ We need this to check if the student is approved!
    @Autowired
    private JoinRequestRepository requestRepository;

    // --- SIGN UP ROUTE ---
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody AppUser newUser) {
        if (userRepository.findByEmail(newUser.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already in use!");
        }
        AppUser savedUser = userRepository.save(newUser);
        return ResponseEntity.ok(savedUser);
    }

    // --- FETCH ALL TEACHERS ROUTE ---
    @GetMapping("/teachers")
    public ResponseEntity<?> getTeachers() {
        return ResponseEntity.ok(userRepository.findByRole("teacher"));
    }

    // --- LOGIN ROUTE (NOW WITH AUTHORIZATION!) ---
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AppUser loginRequest) {
        AppUser existingUser = userRepository.findByEmail(loginRequest.getEmail());
        
        // 1. Check if email and password are correct (Authentication)
        if (existingUser != null && existingUser.getPassword().equals(loginRequest.getPassword())) {
            
            // 2. If it is a student, check their join request status (Authorization)
            if ("student".equals(existingUser.getRole())) {
                JoinRequest req = requestRepository.findByStudentId(existingUser.getId());
                
                if (req != null && "PENDING".equals(req.getStatus())) {
                    // Block them and send a 403 Forbidden!
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access Denied: Your account is still pending educator approval.");
                }
                
                if (req != null && "REJECTED".equals(req.getStatus())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access Denied: Your request to join the class was declined.");
                }
            }
            
            // 3. If they are a teacher, or an APPROVED student, let them in!
            return ResponseEntity.ok(existingUser); 
        }
        
        // Fail: Wrong email or password
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
}