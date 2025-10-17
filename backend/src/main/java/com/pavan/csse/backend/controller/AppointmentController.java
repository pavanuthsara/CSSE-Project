package com.pavan.csse.backend.controller;

import com.pavan.csse.backend.dto.AppointmentBookingRequest;
import com.pavan.csse.backend.dto.AppointmentResponse;
import com.pavan.csse.backend.dto.AvailableTimeSlotResponse;
import com.pavan.csse.backend.model.AppointmentStatus;
import com.pavan.csse.backend.model.Patient;
import com.pavan.csse.backend.model.User;
import com.pavan.csse.backend.repository.PatientRepository;
import com.pavan.csse.backend.repository.UserRepository;
import com.pavan.csse.backend.service.AppointmentService;
import com.pavan.csse.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(
            @RequestHeader("Authorization") String token,
            @RequestBody AppointmentBookingRequest request) {
        try {
            // Extract patient ID from JWT token
            Long patientId = extractPatientIdFromToken(token);
            
            AppointmentResponse response = appointmentService.bookAppointment(patientId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/patient")
    public ResponseEntity<?> getPatientAppointments(
            @RequestHeader("Authorization") String token) {
        try {
            Long patientId = extractPatientIdFromToken(token);
            List<AppointmentResponse> appointments = appointmentService.getPatientAppointments(patientId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/patient/upcoming")
    public ResponseEntity<?> getUpcomingAppointments(
            @RequestHeader("Authorization") String token) {
        try {
            Long patientId = extractPatientIdFromToken(token);
            List<AppointmentResponse> appointments = appointmentService.getUpcomingAppointments(patientId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/doctor/{doctorId}/available-slots")
    public ResponseEntity<?> getAvailableTimeSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<AvailableTimeSlotResponse> slots = appointmentService.getAvailableTimeSlots(doctorId, date);
            return ResponseEntity.ok(slots);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getDoctorAppointments(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<AppointmentResponse> appointments = appointmentService.getDoctorAppointments(doctorId, date);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestParam AppointmentStatus status) {
        try {
            AppointmentResponse response = appointmentService.updateAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId) {
        try {
            appointmentService.cancelAppointment(appointmentId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Appointment cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        try {
            List<AppointmentResponse> appointments = appointmentService.getAppointmentsByStatus(status);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    private Long extractPatientIdFromToken(String token) {
        // Remove "Bearer " prefix
        String jwtToken = token.substring(7);
        
        // Extract username from token
        String username = jwtUtil.extractUsername(jwtToken);
        
        // Get user by username
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Get patient by user
        Patient patient = patientRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Patient profile not found"));
        
        return patient.getId();
    }
}
