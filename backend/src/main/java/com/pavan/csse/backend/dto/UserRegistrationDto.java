package com.pavan.csse.backend.dto;

import com.pavan.csse.backend.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserRole role;
    
    // Doctor specific fields
    private String licenseNumber;
    private String specialization;
    private Integer yearsOfExperience;
    private String department;
    private Double consultationFee;
    private String availableHours;
    
    // Staff specific fields
    private String employeeId;
    private String position;
    private String shiftTimings;
    private Long supervisorId;
}