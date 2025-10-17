package com.pavan.csse.backend.dto;

import com.pavan.csse.backend.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String email;
    private UserRole role;
    private String firstName;
    private String lastName;
}