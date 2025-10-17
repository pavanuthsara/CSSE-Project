package com.pavan.csse.backend.service;

import com.pavan.csse.backend.dto.LoginRequest;
import com.pavan.csse.backend.dto.LoginResponse;
import com.pavan.csse.backend.dto.UserRegistrationDto;
import com.pavan.csse.backend.model.*;
import com.pavan.csse.backend.repository.DoctorRepository;
import com.pavan.csse.backend.repository.StaffRepository;
import com.pavan.csse.backend.repository.UserRepository;
import com.pavan.csse.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            
            // Generate JWT token
            String token = jwtUtil.generateToken(userDetails);
            
            // Get user information
            User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

            return new LoginResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName()
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @Transactional
    public User registerUser(UserRegistrationDto registrationDto) {
        // Check if username already exists
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setRole(registrationDto.getRole());
        user.setIsActive(true);

        // Save user
        User savedUser = userRepository.save(user);

        // Create role-specific profile
        if (registrationDto.getRole() == UserRole.DOCTOR) {
            createDoctorProfile(savedUser, registrationDto);
        } else if (registrationDto.getRole() == UserRole.STAFF) {
            createStaffProfile(savedUser, registrationDto);
        }

        return savedUser;
    }

    private void createDoctorProfile(User user, UserRegistrationDto dto) {
        if (doctorRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new RuntimeException("License number already exists");
        }

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setLicenseNumber(dto.getLicenseNumber());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setYearsOfExperience(dto.getYearsOfExperience());
        doctor.setDepartment(dto.getDepartment());
        doctor.setConsultationFee(dto.getConsultationFee());
        doctor.setAvailableHours(dto.getAvailableHours());

        doctorRepository.save(doctor);
    }

    private void createStaffProfile(User user, UserRegistrationDto dto) {
        if (staffRepository.existsByEmployeeId(dto.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists");
        }

        Staff staff = new Staff();
        staff.setUser(user);
        staff.setEmployeeId(dto.getEmployeeId());
        staff.setPosition(dto.getPosition());
        staff.setDepartment(dto.getDepartment());
        staff.setShiftTimings(dto.getShiftTimings());
        staff.setSupervisorId(dto.getSupervisorId());

        staffRepository.save(staff);
    }
}