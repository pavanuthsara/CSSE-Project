package com.pavan.csse.backend.dto;

import com.pavan.csse.backend.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private PatientInfo patient;
    private DoctorInfo doctor;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Integer durationMinutes;
    private AppointmentStatus status;
    private String reasonForVisit;
    private String notes;
    private LocalDateTime createdAt;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientInfo {
        private Long id;
        private String patientId;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorInfo {
        private Long id;
        private String firstName;
        private String lastName;
        private String specialization;
        private String department;
        private Double consultationFee;
    }
}
