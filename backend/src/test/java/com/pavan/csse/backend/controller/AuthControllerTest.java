package com.pavan.csse.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavan.csse.backend.dto.LoginRequest;
import com.pavan.csse.backend.dto.LoginResponse;
import com.pavan.csse.backend.dto.UserRegistrationDto;
import com.pavan.csse.backend.model.User;
import com.pavan.csse.backend.model.UserRole;
import com.pavan.csse.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    private UserRegistrationDto registrationDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("testuser", "password123");
        
        loginResponse = new LoginResponse();
        loginResponse.setToken("jwt-token");
        loginResponse.setUsername("testuser");
        loginResponse.setEmail("test@example.com");
        loginResponse.setRole(UserRole.DOCTOR);
        loginResponse.setFirstName("Test");
        loginResponse.setLastName("User");

        registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("newuser");
        registrationDto.setPassword("password123");
        registrationDto.setEmail("newuser@example.com");
        registrationDto.setFirstName("New");
        registrationDto.setLastName("User");
        registrationDto.setRole(UserRole.DOCTOR);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("newuser");
        testUser.setRole(UserRole.DOCTOR);
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("DOCTOR"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    void testLoginFailure() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid username or password"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }

    @Test
    void testRegisterSuccess() throws Exception {
        // Arrange
        when(authService.registerUser(any(UserRegistrationDto.class))).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.role").value("DOCTOR"));
    }

    @Test
    void testRegisterFailure() throws Exception {
        // Arrange
        when(authService.registerUser(any(UserRegistrationDto.class)))
                .thenThrow(new RuntimeException("Username already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username already exists"));
    }

    @Test
    void testTestEndpoint() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/auth/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Auth API is working!"));
    }

    @Test
    void testLoginWithInvalidJson() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithMissingFields() throws Exception {
        // Arrange
        LoginRequest incompleteRequest = new LoginRequest();
        incompleteRequest.setUsername("testuser");
        // password is null

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompleteRequest)))
                .andExpect(status().isUnauthorized()); // Assuming the service will throw an error
    }
}