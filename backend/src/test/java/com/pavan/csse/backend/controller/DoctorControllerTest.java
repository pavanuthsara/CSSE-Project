package com.pavan.csse.backend.controller;

import com.pavan.csse.backend.model.Doctor;
import com.pavan.csse.backend.model.User;
import com.pavan.csse.backend.model.UserRole;
import com.pavan.csse.backend.repository.DoctorRepository;
import com.pavan.csse.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorRepository doctorRepository;

    @MockBean
    private UserRepository userRepository;

    private User doctorUser;
    private Doctor doctor;
    private List<Doctor> doctors;

    @BeforeEach
    void setUp() {
        doctorUser = new User();
        doctorUser.setId(1L);
        doctorUser.setUsername("doctor1");
        doctorUser.setEmail("doctor@example.com");
        doctorUser.setFirstName("Dr. John");
        doctorUser.setLastName("Smith");
        doctorUser.setRole(UserRole.DOCTOR);
        doctorUser.setIsActive(true);

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUser(doctorUser);
        doctor.setLicenseNumber("MD123456");
        doctor.setSpecialization("Cardiology");
        doctor.setYearsOfExperience(10);
        doctor.setDepartment("Cardiology");
        doctor.setConsultationFee(150.0);
        doctor.setAvailableHours("9:00 AM - 5:00 PM");

        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setSpecialization("Cardiology");

        doctors = Arrays.asList(doctor, doctor2);
    }

    @Test
    @WithMockUser(username = "doctor1", roles = "DOCTOR")
    void testGetDashboardSuccess() throws Exception {
        // Arrange
        when(userRepository.findByUsername("doctor1")).thenReturn(Optional.of(doctorUser));
        when(doctorRepository.findByUserId(1L)).thenReturn(Optional.of(doctor));

        // Act & Assert
        mockMvc.perform(get("/api/doctor/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Welcome to Doctor Dashboard"))
                .andExpect(jsonPath("$.doctor.licenseNumber").value("MD123456"))
                .andExpect(jsonPath("$.doctor.specialization").value("Cardiology"))
                .andExpect(jsonPath("$.user.username").value("doctor1"));
    }

    @Test
    @WithMockUser(username = "nonexistent", roles = "DOCTOR")
    void testGetDashboardUserNotFound() throws Exception {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/doctor/dashboard"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    @WithMockUser(username = "doctor1", roles = "DOCTOR")
    void testGetDashboardDoctorProfileNotFound() throws Exception {
        // Arrange
        when(userRepository.findByUsername("doctor1")).thenReturn(Optional.of(doctorUser));
        when(doctorRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/doctor/dashboard"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Doctor profile not found"));
    }

    @Test
    @WithMockUser(username = "doctor1", roles = "DOCTOR")
    void testGetProfileSuccess() throws Exception {
        // Arrange
        when(userRepository.findByUsername("doctor1")).thenReturn(Optional.of(doctorUser));
        when(doctorRepository.findByUserId(1L)).thenReturn(Optional.of(doctor));

        // Act & Assert
        mockMvc.perform(get("/api/doctor/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licenseNumber").value("MD123456"))
                .andExpect(jsonPath("$.specialization").value("Cardiology"))
                .andExpect(jsonPath("$.yearsOfExperience").value(10))
                .andExpect(jsonPath("$.consultationFee").value(150.0));
    }

    @Test
    @WithMockUser(username = "doctor1", roles = "DOCTOR")
    void testGetProfileDoctorNotFound() throws Exception {
        // Arrange
        when(userRepository.findByUsername("doctor1")).thenReturn(Optional.of(doctorUser));
        when(doctorRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/doctor/profile"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Doctor profile not found"));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void testGetAllDoctors() throws Exception {
        // Arrange
        when(doctorRepository.findAll()).thenReturn(doctors);

        // Act & Assert
        mockMvc.perform(get("/api/doctor/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void testGetDoctorsBySpecialization() throws Exception {
        // Arrange
        when(doctorRepository.findBySpecialization("Cardiology")).thenReturn(doctors);

        // Act & Assert
        mockMvc.perform(get("/api/doctor/specialization/Cardiology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void testGetAllDoctorsException() throws Exception {
        // Arrange
        when(doctorRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/api/doctor/all"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Database error"));
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        // Act & Assert - No authentication
        mockMvc.perform(get("/api/doctor/dashboard"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "staff1", roles = "STAFF")
    void testWrongRoleAccess() throws Exception {
        // Act & Assert - Staff trying to access doctor endpoint
        mockMvc.perform(get("/api/doctor/dashboard"))
                .andExpect(status().isForbidden());
    }
}