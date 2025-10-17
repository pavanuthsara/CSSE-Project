package com.pavan.csse.backend.controller;

import com.pavan.csse.backend.dto.LoginResponse;
import com.pavan.csse.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestController.class)
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginResponse = new LoginResponse();
        loginResponse.setToken("test-jwt-token");
        loginResponse.setUsername("doctor1");
        loginResponse.setEmail("doctor@example.com");
        loginResponse.setFirstName("Dr. John");
        loginResponse.setLastName("Smith");
    }

    @Test
    void testGetTestCredentials() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/test/credentials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test login credentials"))
                .andExpect(jsonPath("$.credentials").isArray())
                .andExpect(jsonPath("$.credentials.length()").value(3))
                .andExpect(jsonPath("$.credentials[0].role").value("DOCTOR"))
                .andExpect(jsonPath("$.credentials[0].username").value("doctor1"))
                .andExpect(jsonPath("$.credentials[0].password").value("password123"))
                .andExpect(jsonPath("$.credentials[1].role").value("STAFF"))
                .andExpect(jsonPath("$.credentials[1].username").value("staff1"))
                .andExpect(jsonPath("$.credentials[2].role").value("ADMIN"))
                .andExpect(jsonPath("$.credentials[2].username").value("admin1"))
                .andExpect(jsonPath("$.note").value("Use POST /api/auth/login with these credentials"));
    }

    @Test
    void testQuickLoginDoctorSuccess() throws Exception {
        // Arrange
        when(authService.login(any())).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/test/quick-login/doctor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"))
                .andExpect(jsonPath("$.username").value("doctor1"))
                .andExpect(jsonPath("$.email").value("doctor@example.com"));
    }

    @Test
    void testQuickLoginStaffSuccess() throws Exception {
        // Arrange
        when(authService.login(any())).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/test/quick-login/staff"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"));
    }

    @Test
    void testQuickLoginAdminSuccess() throws Exception {
        // Arrange
        when(authService.login(any())).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/test/quick-login/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"));
    }

    @Test
    void testQuickLoginDoctorFailure() throws Exception {
        // Arrange
        when(authService.login(any())).thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        mockMvc.perform(post("/api/test/quick-login/doctor"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Authentication failed"));
    }

    @Test
    void testQuickLoginStaffFailure() throws Exception {
        // Arrange
        when(authService.login(any())).thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        mockMvc.perform(post("/api/test/quick-login/staff"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    void testGetSystemStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/test/system-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ONLINE"))
                .andExpect(jsonPath("$.message").value("Backend system is running"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.available_endpoints").exists())
                .andExpect(jsonPath("$.available_endpoints.auth").exists())
                .andExpect(jsonPath("$.available_endpoints.doctor").exists())
                .andExpect(jsonPath("$.available_endpoints.staff").exists())
                .andExpect(jsonPath("$.available_endpoints.test").exists());
    }

    @Test
    void testSystemStatusContent() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/test/system-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available_endpoints.auth").value("/api/auth/login, /api/auth/register"))
                .andExpect(jsonPath("$.available_endpoints.doctor").value("/api/doctor/dashboard, /api/doctor/profile"))
                .andExpect(jsonPath("$.available_endpoints.staff").value("/api/staff/dashboard, /api/staff/profile"))
                .andExpect(jsonPath("$.available_endpoints.test").value("/api/test/credentials, /api/test/quick-login/{role}"));
    }
}