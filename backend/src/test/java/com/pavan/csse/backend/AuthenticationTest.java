package com.pavan.csse.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavan.csse.backend.dto.LoginRequest;
import com.pavan.csse.backend.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestMvc
class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetTestCredentials() throws Exception {
        mockMvc.perform(get("/api/test/credentials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test login credentials"))
                .andExpect(jsonPath("$.credentials").isArray())
                .andExpect(jsonPath("$.credentials[0].role").value("DOCTOR"))
                .andExpect(jsonPath("$.credentials[1].role").value("STAFF"))
                .andExpect(jsonPath("$.credentials[2].role").value("ADMIN"));
    }

    @Test
    void testSystemStatus() throws Exception {
        mockMvc.perform(get("/api/test/system-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ONLINE"))
                .andExpect(jsonPath("$.message").value("Backend system is running"));
    }

    @Test
    void testDoctorLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("doctor1", "password123");
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("doctor1"))
                .andExpect(jsonPath("$.role").value("DOCTOR"));
    }

    @Test
    void testStaffLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("staff1", "password123");
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("staff1"))
                .andExpect(jsonPath("$.role").value("STAFF"));
    }

    @Test
    void testAdminLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin1", "password123");
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("admin1"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void testQuickLoginDoctor() throws Exception {
        mockMvc.perform(post("/api/test/quick-login/doctor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("doctor1"))
                .andExpect(jsonPath("$.role").value("DOCTOR"));
    }

    @Test
    void testQuickLoginStaff() throws Exception {
        mockMvc.perform(post("/api/test/quick-login/staff"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("staff1"))
                .andExpect(jsonPath("$.role").value("STAFF"));
    }

    @Test
    void testInvalidLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("invalid", "invalid");
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").exists());
    }
}