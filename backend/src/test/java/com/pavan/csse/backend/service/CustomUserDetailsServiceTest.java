package com.pavan.csse.backend.service;

import com.pavan.csse.backend.model.User;
import com.pavan.csse.backend.model.UserRole;
import com.pavan.csse.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    
    private User testUser;
    
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
        testUser.setIsActive(true);
    }
    
    @Test
    void testLoadUserByUsernameSuccess() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");
        
        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_DOCTOR")));
        
        verify(userRepository).findByUsername("testuser");
    }
    
    @Test
    void testLoadUserByUsernameNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        
        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("nonexistent"));
        
        assertEquals("User not found: nonexistent", exception.getMessage());
        verify(userRepository).findByUsername("nonexistent");
    }
    
    @Test
    void testLoadUserByUsernameWithInactiveUser() {
        // Arrange
        testUser.setIsActive(false);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");
        
        // Assert
        assertNotNull(userDetails);
        assertFalse(userDetails.isEnabled());
        verify(userRepository).findByUsername("testuser");
    }
}