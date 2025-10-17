package com.pavan.csse.backend.service;

import com.pavan.csse.backend.model.*;
import com.pavan.csse.backend.repository.DoctorRepository;
import com.pavan.csse.backend.repository.StaffRepository;
import com.pavan.csse.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeTestUsers();
    }

    private void initializeTestUsers() {
        // Create test doctor if not exists
        if (!userRepository.existsByUsername("doctor1")) {
            createTestDoctor();
        }

        // Create test staff if not exists
        if (!userRepository.existsByUsername("staff1")) {
            createTestStaff();
        }

        // Create test admin if not exists
        if (!userRepository.existsByUsername("admin1")) {
            createTestAdmin();
        }

        System.out.println("=== TEST LOGIN CREDENTIALS ===");
        System.out.println("Doctor Login:");
        System.out.println("  Username: doctor1");
        System.out.println("  Password: password123");
        System.out.println("");
        System.out.println("Staff Login:");
        System.out.println("  Username: staff1");
        System.out.println("  Password: password123");
        System.out.println("");
        System.out.println("Admin Login:");
        System.out.println("  Username: admin1");
        System.out.println("  Password: password123");
        System.out.println("==============================");
    }

    private void createTestDoctor() {
        // Create User
        User doctorUser = new User();
        doctorUser.setUsername("doctor1");
        doctorUser.setPassword(passwordEncoder.encode("password123"));
        doctorUser.setEmail("doctor@hospital.com");
        doctorUser.setFirstName("Dr. John");
        doctorUser.setLastName("Smith");
        doctorUser.setPhoneNumber("+1234567890");
        doctorUser.setRole(UserRole.DOCTOR);
        doctorUser.setIsActive(true);
        doctorUser.setCreatedAt(LocalDateTime.now());
        doctorUser.setUpdatedAt(LocalDateTime.now());

        User savedDoctorUser = userRepository.save(doctorUser);

        // Create Doctor profile
        Doctor doctor = new Doctor();
        doctor.setUser(savedDoctorUser);
        doctor.setLicenseNumber("MD123456");
        doctor.setSpecialization("Cardiology");
        doctor.setYearsOfExperience(10);
        doctor.setDepartment("Cardiology");
        doctor.setConsultationFee(150.0);
        doctor.setAvailableHours("9:00 AM - 5:00 PM");

        doctorRepository.save(doctor);
    }

    private void createTestStaff() {
        // Create User
        User staffUser = new User();
        staffUser.setUsername("staff1");
        staffUser.setPassword(passwordEncoder.encode("password123"));
        staffUser.setEmail("nurse@hospital.com");
        staffUser.setFirstName("Jane");
        staffUser.setLastName("Doe");
        staffUser.setPhoneNumber("+1234567891");
        staffUser.setRole(UserRole.STAFF);
        staffUser.setIsActive(true);
        staffUser.setCreatedAt(LocalDateTime.now());
        staffUser.setUpdatedAt(LocalDateTime.now());

        User savedStaffUser = userRepository.save(staffUser);

        // Create Staff profile
        Staff staff = new Staff();
        staff.setUser(savedStaffUser);
        staff.setEmployeeId("EMP001");
        staff.setPosition("Registered Nurse");
        staff.setDepartment("Emergency");
        staff.setShiftTimings("8:00 AM - 4:00 PM");
        staff.setSupervisorId(null);

        staffRepository.save(staff);
    }

    private void createTestAdmin() {
        // Create User
        User adminUser = new User();
        adminUser.setUsername("admin1");
        adminUser.setPassword(passwordEncoder.encode("password123"));
        adminUser.setEmail("admin@hospital.com");
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setPhoneNumber("+1234567892");
        adminUser.setRole(UserRole.ADMIN);
        adminUser.setIsActive(true);
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());

        userRepository.save(adminUser);
    }
}