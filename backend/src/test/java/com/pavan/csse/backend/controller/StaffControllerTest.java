package com.pavan.csse.backend.controller;

import com.pavan.csse.backend.model.Staff;
import com.pavan.csse.backend.model.User;
import com.pavan.csse.backend.model.UserRole;
import com.pavan.csse.backend.repository.StaffRepository;
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

@WebMvcTest(StaffController.class)
class StaffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StaffRepository staffRepository;

    @MockBean
    private UserRepository userRepository;

    private User staffUser;
    private Staff staff;
    private List<Staff> staffList;

    @BeforeEach
    void setUp() {
        staffUser = new User();
        staffUser.setId(1L);
        staffUser.setUsername("staff1");
        staffUser.setEmail("staff@example.com");
        staffUser.setFirstName("Jane");
        staffUser.setLastName("Doe");
        staffUser.setRole(UserRole.STAFF);
        staffUser.setIsActive(true);

        staff = new Staff();
        staff.setId(1L);
        staff.setUser(staffUser);
        staff.setEmployeeId("EMP001");
        staff.setPosition("Registered Nurse");
        staff.setDepartment("Emergency");
        staff.setShiftTimings("8:00 AM - 4:00 PM");

        Staff staff2 = new Staff();
        staff2.setId(2L);
        staff2.setDepartment("Emergency");

        staffList = Arrays.asList(staff, staff2);
    }

    @Test
    @WithMockUser(username = "staff1", roles = "STAFF")
    void testGetDashboardSuccess() throws Exception {
        // Arrange
        when(userRepository.findByUsername("staff1")).thenReturn(Optional.of(staffUser));
        when(staffRepository.findByUserId(1L)).thenReturn(Optional.of(staff));

        // Act & Assert
        mockMvc.perform(get("/api/staff/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Welcome to Staff Dashboard"))
                .andExpect(jsonPath("$.staff.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.staff.position").value("Registered Nurse"))
                .andExpect(jsonPath("$.user.username").value("staff1"));
    }

    @Test
    @WithMockUser(username = "nonexistent", roles = "STAFF")
    void testGetDashboardUserNotFound() throws Exception {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/staff/dashboard"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    @WithMockUser(username = "staff1", roles = "STAFF")
    void testGetDashboardStaffProfileNotFound() throws Exception {
        // Arrange
        when(userRepository.findByUsername("staff1")).thenReturn(Optional.of(staffUser));
        when(staffRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/staff/dashboard"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Staff profile not found"));
    }

    @Test
    @WithMockUser(username = "staff1", roles = "STAFF")
    void testGetProfileSuccess() throws Exception {
        // Arrange
        when(userRepository.findByUsername("staff1")).thenReturn(Optional.of(staffUser));
        when(staffRepository.findByUserId(1L)).thenReturn(Optional.of(staff));

        // Act & Assert
        mockMvc.perform(get("/api/staff/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.position").value("Registered Nurse"))
                .andExpect(jsonPath("$.department").value("Emergency"))
                .andExpect(jsonPath("$.shiftTimings").value("8:00 AM - 4:00 PM"));
    }

    @Test
    @WithMockUser(username = "staff1", roles = "STAFF")
    void testGetProfileStaffNotFound() throws Exception {
        // Arrange
        when(userRepository.findByUsername("staff1")).thenReturn(Optional.of(staffUser));
        when(staffRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/staff/profile"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Staff profile not found"));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void testGetAllStaff() throws Exception {
        // Arrange
        when(staffRepository.findAll()).thenReturn(staffList);

        // Act & Assert
        mockMvc.perform(get("/api/staff/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void testGetStaffByDepartment() throws Exception {
        // Arrange
        when(staffRepository.findByDepartment("Emergency")).thenReturn(staffList);

        // Act & Assert
        mockMvc.perform(get("/api/staff/department/Emergency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void testGetAllStaffException() throws Exception {
        // Arrange
        when(staffRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/api/staff/all"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Database error"));
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        // Act & Assert - No authentication
        mockMvc.perform(get("/api/staff/dashboard"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "doctor1", roles = "DOCTOR")
    void testWrongRoleAccess() throws Exception {
        // Act & Assert - Doctor trying to access staff endpoint
        mockMvc.perform(get("/api/staff/dashboard"))
                .andExpect(status().isForbidden());
    }
}