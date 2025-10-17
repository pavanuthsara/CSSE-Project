package com.pavan.csse.backend.controller;

import com.pavan.csse.backend.dto.LoginRequest;
import com.pavan.csse.backend.dto.LoginResponse;
import com.pavan.csse.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private AuthService authService;

    @GetMapping("/credentials")
    public ResponseEntity<?> getTestCredentials() {
        List<Map<String, String>> credentials = new ArrayList<>();
        
        Map<String, String> doctor = new HashMap<>();
        doctor.put("role", "DOCTOR");
        doctor.put("username", "doctor1");
        doctor.put("password", "password123");
        doctor.put("description", "Test doctor account - Dr. John Smith, Cardiology specialist");
        credentials.add(doctor);

        Map<String, String> staff = new HashMap<>();
        staff.put("role", "STAFF");
        staff.put("username", "staff1");
        staff.put("password", "password123");
        staff.put("description", "Test staff account - Jane Doe, Registered Nurse in Emergency");
        credentials.add(staff);

        Map<String, String> admin = new HashMap<>();
        admin.put("role", "ADMIN");
        admin.put("username", "admin1");
        admin.put("password", "password123");
        admin.put("description", "Test admin account - System administrator");
        credentials.add(admin);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Test login credentials");
        response.put("credentials", credentials);
        response.put("note", "Use POST /api/auth/login with these credentials");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/quick-login/doctor")
    public ResponseEntity<?> quickLoginDoctor() {
        LoginRequest request = new LoginRequest("doctor1", "password123");
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/quick-login/staff")
    public ResponseEntity<?> quickLoginStaff() {
        LoginRequest request = new LoginRequest("staff1", "password123");
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/quick-login/admin")
    public ResponseEntity<?> quickLoginAdmin() {
        LoginRequest request = new LoginRequest("admin1", "password123");
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/system-status")
    public ResponseEntity<?> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "ONLINE");
        status.put("message", "Backend system is running");
        status.put("timestamp", java.time.LocalDateTime.now());
        status.put("available_endpoints", Map.of(
            "auth", "/api/auth/login, /api/auth/register",
            "doctor", "/api/doctor/dashboard, /api/doctor/profile",
            "staff", "/api/staff/dashboard, /api/staff/profile",
            "test", "/api/test/credentials, /api/test/quick-login/{role}"
        ));
        
        return ResponseEntity.ok(status);
    }
}