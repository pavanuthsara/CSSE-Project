package com.pavan.csse.backend.controller;

import com.pavan.csse.backend.dto.PatientRegistrationDto;
import com.pavan.csse.backend.model.Patient;
import com.pavan.csse.backend.model.User;
import com.pavan.csse.backend.model.UserRole;
import com.pavan.csse.backend.repository.PatientRepository;
import com.pavan.csse.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(@RequestBody PatientRegistrationDto registrationDto) {
        try {
            // Check if username already exists
            if (userRepository.existsByUsername(registrationDto.getUsername())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username already exists");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Check if email already exists
            if (userRepository.existsByEmail(registrationDto.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email already exists");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Create User
            User user = new User();
            user.setUsername(registrationDto.getUsername());
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            user.setEmail(registrationDto.getEmail());
            user.setFirstName(registrationDto.getFirstName());
            user.setLastName(registrationDto.getLastName());
            user.setPhoneNumber(registrationDto.getPhoneNumber());
            user.setRole(UserRole.PATIENT);
            user.setIsActive(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            
            User savedUser = userRepository.save(user);
            
            // Create Patient
            Patient patient = new Patient();
            patient.setUser(savedUser);
            patient.setPatientId(generatePatientId());
            patient.setDateOfBirth(registrationDto.getDateOfBirth());
            patient.setGender(registrationDto.getGender());
            patient.setAddress(registrationDto.getAddress());
            patient.setEmergencyContact(registrationDto.getEmergencyContact());
            patient.setMedicalHistory(registrationDto.getMedicalHistory());
            patient.setAllergies(registrationDto.getAllergies());
            patient.setInsuranceProvider(registrationDto.getInsuranceProvider());
            patient.setInsuranceNumber(registrationDto.getInsuranceNumber());
            patient.setCreatedAt(LocalDateTime.now());
            patient.setUpdatedAt(LocalDateTime.now());
            
            Patient savedPatient = patientRepository.save(patient);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Patient registered successfully");
            response.put("patientId", savedPatient.getId());
            response.put("username", savedUser.getUsername());
            response.put("role", savedUser.getRole());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getPatientProfile(@RequestHeader("Authorization") String token) {
        try {
            // Extract username from token and get patient profile
            // This is a simplified implementation
            Map<String, String> error = new HashMap<>();
            error.put("error", "Profile retrieval not implemented yet");
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve profile: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllPatients() {
        try {
            List<Patient> patients = patientRepository.findAll();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve patients: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    private String generatePatientId() {
        // Generate a unique patient ID like PAT001, PAT002, etc.
        long count = patientRepository.count();
        return String.format("PAT%03d", count + 1);
    }
}
