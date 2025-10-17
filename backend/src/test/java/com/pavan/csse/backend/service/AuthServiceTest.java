package com.pavan.csse.backend.service;

import com.pavan.csse.backend.dto.LoginRequest;
import com.pavan.csse.backend.dto.LoginResponse;
import com.pavan.csse.backend.dto.UserRegistrationDto;
import com.pavan.csse.backend.model.*;
import com.pavan.csse.backend.repository.DoctorRepository;
import com.pavan.csse.backend.repository.StaffRepository;
import com.pavan.csse.backend.repository.UserRepository;
import com.pavan.csse.backend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private DoctorRepository doctorRepository;
    
    @Mock
    private StaffRepository staffRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @Mock
    private CustomUserDetailsService userDetailsService;
    
    @Mock
    private Authentication authentication;
    
    @InjectMocks
    private AuthService authService;
    
    private User testUser;
    private LoginRequest loginRequest;
    private UserRegistrationDto doctorRegistrationDto;
    private UserRegistrationDto staffRegistrationDto;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setRole(UserRole.DOCTOR);
        
        loginRequest = new LoginRequest("testuser", "password123");
        
        doctorRegistrationDto = new UserRegistrationDto();
        doctorRegistrationDto.setUsername("newdoctor");
        doctorRegistrationDto.setPassword("password123");
        doctorRegistrationDto.setEmail("doctor@example.com");
        doctorRegistrationDto.setFirstName("New");
        doctorRegistrationDto.setLastName("Doctor");
        doctorRegistrationDto.setRole(UserRole.DOCTOR);
        doctorRegistrationDto.setLicenseNumber("MD12345");
        doctorRegistrationDto.setSpecialization("Cardiology");
        
        staffRegistrationDto = new UserRegistrationDto();
        staffRegistrationDto.setUsername("newstaff");
        staffRegistrationDto.setPassword("password123");
        staffRegistrationDto.setEmail("staff@example.com");
        staffRegistrationDto.setFirstName("New");
        staffRegistrationDto.setLastName("Staff");
        staffRegistrationDto.setRole(UserRole.STAFF);
        staffRegistrationDto.setEmployeeId("EMP123");
        staffRegistrationDto.setPosition("Nurse");
    }
    
    @Test
    void testLoginSuccess() {
        // Arrange
        String token = "jwt-token";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userDetailsService.loadUserByUsername(loginRequest.getUsername()))
                .thenReturn(testUser);
        when(jwtUtil.generateToken(testUser)).thenReturn(token);
        when(userRepository.findByUsername(loginRequest.getUsername()))
                .thenReturn(Optional.of(testUser));
        
        // Act
        LoginResponse response = authService.login(loginRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(token, response.getToken());
        assertEquals(testUser.getUsername(), response.getUsername());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getRole(), response.getRole());
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(testUser);
    }
    
    @Test
    void testLoginFailure() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> authService.login(loginRequest));
        assertEquals("Invalid username or password", exception.getMessage());
    }
    
    @Test
    void testRegisterDoctorSuccess() {
        // Arrange
        when(userRepository.existsByUsername(doctorRegistrationDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(doctorRegistrationDto.getEmail())).thenReturn(false);
        when(doctorRepository.existsByLicenseNumber(doctorRegistrationDto.getLicenseNumber())).thenReturn(false);
        when(passwordEncoder.encode(doctorRegistrationDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(new Doctor());
        
        // Act
        User result = authService.registerUser(doctorRegistrationDto);
        
        // Assert
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(doctorRepository).save(any(Doctor.class));
    }
    
    @Test
    void testRegisterStaffSuccess() {
        // Arrange
        when(userRepository.existsByUsername(staffRegistrationDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(staffRegistrationDto.getEmail())).thenReturn(false);
        when(staffRepository.existsByEmployeeId(staffRegistrationDto.getEmployeeId())).thenReturn(false);
        when(passwordEncoder.encode(staffRegistrationDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(staffRepository.save(any(Staff.class))).thenReturn(new Staff());
        
        // Act
        User result = authService.registerUser(staffRegistrationDto);
        
        // Assert
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(staffRepository).save(any(Staff.class));
    }
    
    @Test
    void testRegisterUserWithExistingUsername() {
        // Arrange
        when(userRepository.existsByUsername(doctorRegistrationDto.getUsername())).thenReturn(true);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> authService.registerUser(doctorRegistrationDto));
        assertEquals("Username already exists", exception.getMessage());
    }
    
    @Test
    void testRegisterUserWithExistingEmail() {
        // Arrange
        when(userRepository.existsByUsername(doctorRegistrationDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(doctorRegistrationDto.getEmail())).thenReturn(true);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> authService.registerUser(doctorRegistrationDto));
        assertEquals("Email already exists", exception.getMessage());
    }
    
    @Test
    void testRegisterDoctorWithExistingLicenseNumber() {
        // Arrange
        when(userRepository.existsByUsername(doctorRegistrationDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(doctorRegistrationDto.getEmail())).thenReturn(false);
        when(doctorRepository.existsByLicenseNumber(doctorRegistrationDto.getLicenseNumber())).thenReturn(true);
        when(passwordEncoder.encode(doctorRegistrationDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> authService.registerUser(doctorRegistrationDto));
        assertEquals("License number already exists", exception.getMessage());
    }
    
    @Test
    void testRegisterStaffWithExistingEmployeeId() {
        // Arrange
        when(userRepository.existsByUsername(staffRegistrationDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(staffRegistrationDto.getEmail())).thenReturn(false);
        when(staffRepository.existsByEmployeeId(staffRegistrationDto.getEmployeeId())).thenReturn(true);
        when(passwordEncoder.encode(staffRegistrationDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> authService.registerUser(staffRegistrationDto));
        assertEquals("Employee ID already exists", exception.getMessage());
    }
}