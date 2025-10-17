package com.pavan.csse.backend.service;

import com.pavan.csse.backend.model.*;
import com.pavan.csse.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeTestUsers();
        initializeTimeSlots();
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

        // Create test patient if not exists
        if (!userRepository.existsByUsername("patient1")) {
            createTestPatient();
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
        System.out.println("");
        System.out.println("Patient Login:");
        System.out.println("  Username: patient1");
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

    private void createTestPatient() {
        // Create User
        User patientUser = new User();
        patientUser.setUsername("patient1");
        patientUser.setPassword(passwordEncoder.encode("password123"));
        patientUser.setEmail("patient@hospital.com");
        patientUser.setFirstName("John");
        patientUser.setLastName("Patient");
        patientUser.setPhoneNumber("+1234567893");
        patientUser.setRole(UserRole.PATIENT);
        patientUser.setIsActive(true);
        patientUser.setCreatedAt(LocalDateTime.now());
        patientUser.setUpdatedAt(LocalDateTime.now());

        User savedPatientUser = userRepository.save(patientUser);

        // Create Patient profile
        Patient patient = new Patient();
        patient.setUser(savedPatientUser);
        patient.setPatientId("PAT001");
        patient.setDateOfBirth(LocalDate.of(1985, 5, 15));
        patient.setGender("Male");
        patient.setAddress("123 Main St, City, State 12345");
        patient.setEmergencyContact("+1234567894");
        patient.setMedicalHistory("No significant medical history");
        patient.setAllergies("None known");
        patient.setInsuranceProvider("Health Insurance Co.");
        patient.setInsuranceNumber("INS123456789");
        patient.setCreatedAt(LocalDateTime.now());
        patient.setUpdatedAt(LocalDateTime.now());

        patientRepository.save(patient);
    }

    private void initializeTimeSlots() {
        // Get the test doctor
        Doctor doctor = doctorRepository.findAll().stream()
            .filter(d -> d.getUser().getUsername().equals("doctor1"))
            .findFirst()
            .orElse(null);

        if (doctor != null) {
            // Create time slots for the next 30 days
            LocalDate startDate = LocalDate.now();
            for (int i = 0; i < 30; i++) {
                LocalDate currentDate = startDate.plusDays(i);
                
                // Skip weekends (Saturday = 6, Sunday = 7)
                if (currentDate.getDayOfWeek().getValue() <= 5) {
                    createTimeSlotsForDate(doctor, currentDate);
                }
            }
        }
    }

    private void createTimeSlotsForDate(Doctor doctor, LocalDate date) {
        // Create time slots from 9 AM to 5 PM with 30-minute intervals
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        
        while (startTime.isBefore(endTime)) {
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setDoctor(doctor);
            timeSlot.setSlotDate(date);
            timeSlot.setStartTime(startTime);
            timeSlot.setEndTime(startTime.plusMinutes(30));
            timeSlot.setIsAvailable(true);
            timeSlot.setSlotDurationMinutes(30);
            
            timeSlotRepository.save(timeSlot);
            startTime = startTime.plusMinutes(30);
        }
    }
}