package com.pavan.csse.backend.controller;

import com.pavan.csse.backend.model.Staff;
import com.pavan.csse.backend.model.User;
import com.pavan.csse.backend.repository.StaffRepository;
import com.pavan.csse.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            Optional<Staff> staffOpt = staffRepository.findByUserId(user.getId());
            if (staffOpt.isPresent()) {
                Staff staff = staffOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Welcome to Staff Dashboard");
                response.put("staff", staff);
                response.put("user", user);
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Staff profile not found");
                return ResponseEntity.badRequest().body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            Optional<Staff> staffOpt = staffRepository.findByUserId(user.getId());
            if (staffOpt.isPresent()) {
                return ResponseEntity.ok(staffOpt.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Staff profile not found");
                return ResponseEntity.badRequest().body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllStaff() {
        try {
            List<Staff> staff = staffRepository.findAll();
            return ResponseEntity.ok(staff);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<?> getStaffByDepartment(@PathVariable String department) {
        try {
            List<Staff> staff = staffRepository.findByDepartment(department);
            return ResponseEntity.ok(staff);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}