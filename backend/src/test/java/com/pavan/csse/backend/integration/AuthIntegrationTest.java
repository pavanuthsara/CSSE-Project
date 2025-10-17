package com.pavan.csse.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavan.csse.backend.dto.LoginRequest;
import com.pavan.csse.backend.dto.UserRegistrationDto;
import com.pavan.csse.backend.model.UserRole;
import com.pavan.csse.backend.repository.DoctorRepository;
import com.pavan.csse.backend.repository.StaffRepository;
import com.pavan.csse.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private StaffRepository staffRepository;

    @BeforeEach
    void setUp() {
        // Clean up database before each test
        doctorRepository.deleteAll();
        staffRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCompleteAuthenticationFlow() throws Exception {
        // Step 1: Register a new doctor
        UserRegistrationDto doctorDto = new UserRegistrationDto();
        doctorDto.setUsername("integrationdoctor");
        doctorDto.setPassword("password123");
        doctorDto.setEmail("integration@doctor.com");
        doctorDto.setFirstName("Integration");
        doctorDto.setLastName("Doctor");
        doctorDto.setPhoneNumber("+1234567890");
        doctorDto.setRole(UserRole.DOCTOR);
        doctorDto.setLicenseNumber("INT123456");
        doctorDto.setSpecialization("Integration Medicine");
        doctorDto.setYearsOfExperience(5);
        doctorDto.setDepartment("Integration");
        doctorDto.setConsultationFee(100.0);
        doctorDto.setAvailableHours("9 AM - 5 PM");

        // Register the doctor
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.username").value("integrationdoctor"))
                .andExpect(jsonPath("$.role").value("DOCTOR"));

        // Verify user exists in database
        assertTrue(userRepository.existsByUsername("integrationdoctor"));
        assertTrue(doctorRepository.existsByLicenseNumber("INT123456"));

        // Step 2: Login with the registered doctor
        LoginRequest loginRequest = new LoginRequest("integrationdoctor", "password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("integrationdoctor"))
                .andExpect(jsonPath("$.role").value("DOCTOR"))
                .andReturn();

        // Extract token from login response
        String loginResponse = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(loginResponse).get("token").asText();

        // Step 3: Access protected doctor endpoint with token
        mockMvc.perform(get("/api/doctor/dashboard")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Welcome to Doctor Dashboard"))
                .andExpect(jsonPath("$.doctor.licenseNumber").value("INT123456"))
                .andExpect(jsonPath("$.user.username").value("integrationdoctor"));

        // Step 4: Access doctor profile
        mockMvc.perform(get("/api/doctor/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licenseNumber").value("INT123456"))
                .andExpect(jsonPath("$.specialization").value("Integration Medicine"));

        // Step 5: Try to access staff endpoint (should fail)
        mockMvc.perform(get("/api/staff/dashboard")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCompleteStaffAuthenticationFlow() throws Exception {
        // Step 1: Register a new staff member
        UserRegistrationDto staffDto = new UserRegistrationDto();
        staffDto.setUsername("integrationstaff");
        staffDto.setPassword("password123");
        staffDto.setEmail("integration@staff.com");
        staffDto.setFirstName("Integration");
        staffDto.setLastName("Staff");
        staffDto.setPhoneNumber("+1234567891");
        staffDto.setRole(UserRole.STAFF);
        staffDto.setEmployeeId("INT001");
        staffDto.setPosition("Integration Nurse");
        staffDto.setDepartment("Integration");
        staffDto.setShiftTimings("8 AM - 4 PM");

        // Register the staff
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(staffDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.username").value("integrationstaff"))
                .andExpect(jsonPath("$.role").value("STAFF"));

        // Step 2: Login and access staff endpoints
        LoginRequest loginRequest = new LoginRequest("integrationstaff", "password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("integrationstaff"))
                .andExpect(jsonPath("$.role").value("STAFF"))
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token").asText();

        // Step 3: Access staff dashboard
        mockMvc.perform(get("/api/staff/dashboard")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Welcome to Staff Dashboard"))
                .andExpect(jsonPath("$.staff.employeeId").value("INT001"));

        // Step 4: Try to access doctor endpoint (should fail)
        mockMvc.perform(get("/api/doctor/dashboard")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testRegistrationValidation() throws Exception {
        // Test duplicate username
        UserRegistrationDto dto1 = createValidDoctorDto();
        UserRegistrationDto dto2 = createValidDoctorDto();
        dto2.setEmail("different@email.com");
        dto2.setLicenseNumber("DIF123456");

        // Register first user
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        // Try to register with same username
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username already exists"));
    }

    @Test
    void testInvalidLogin() throws Exception {
        // Test login with non-existent user
        LoginRequest loginRequest = new LoginRequest("nonexistent", "password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        // Try to access protected endpoint without token
        mockMvc.perform(get("/api/doctor/dashboard"))
                .andExpect(status().isUnauthorized());

        // Try with invalid token
        mockMvc.perform(get("/api/doctor/dashboard")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testTestEndpoints() throws Exception {
        // Test public test endpoints
        mockMvc.perform(get("/api/test/system-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ONLINE"));

        mockMvc.perform(get("/api/test/credentials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.credentials").isArray());

        // Test quick login endpoints
        mockMvc.perform(post("/api/test/quick-login/doctor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    private UserRegistrationDto createValidDoctorDto() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("testdoctor");
        dto.setPassword("password123");
        dto.setEmail("test@doctor.com");
        dto.setFirstName("Test");
        dto.setLastName("Doctor");
        dto.setPhoneNumber("+1234567890");
        dto.setRole(UserRole.DOCTOR);
        dto.setLicenseNumber("TST123456");
        dto.setSpecialization("Test Medicine");
        dto.setYearsOfExperience(1);
        dto.setDepartment("Testing");
        dto.setConsultationFee(50.0);
        dto.setAvailableHours("9 AM - 5 PM");
        return dto;
    }
}