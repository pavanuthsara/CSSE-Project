package com.pavan.csse.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhoneNumber("+1234567890");
        user.setRole(UserRole.DOCTOR);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testUserCreation() {
        // Assert
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("+1234567890", user.getPhoneNumber());
        assertEquals(UserRole.DOCTOR, user.getRole());
        assertTrue(user.getIsActive());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void testUserDetailsImplementation() {
        // Test UserDetails methods
        assertEquals("testuser", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.isEnabled());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testUserAuthorities() {
        // Act
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_DOCTOR")));
    }

    @Test
    void testUserAuthoritiesWithDifferentRoles() {
        // Test with STAFF role
        user.setRole(UserRole.STAFF);
        Collection<? extends GrantedAuthority> staffAuthorities = user.getAuthorities();
        assertTrue(staffAuthorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STAFF")));

        // Test with ADMIN role
        user.setRole(UserRole.ADMIN);
        Collection<? extends GrantedAuthority> adminAuthorities = user.getAuthorities();
        assertTrue(adminAuthorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testIsEnabledWithInactiveUser() {
        // Arrange
        user.setIsActive(false);

        // Act & Assert
        assertFalse(user.isEnabled());
    }

    @Test
    void testIsEnabledWithActiveUser() {
        // Arrange
        user.setIsActive(true);

        // Act & Assert
        assertTrue(user.isEnabled());
    }

    @Test
    void testPrePersistCallback() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("password");
        newUser.setEmail("new@example.com");
        newUser.setFirstName("New");
        newUser.setLastName("User");
        newUser.setRole(UserRole.DOCTOR);

        // Simulate @PrePersist
        LocalDateTime beforePersist = LocalDateTime.now();
        newUser.onCreate();

        // Assert
        assertNotNull(newUser.getCreatedAt());
        assertNotNull(newUser.getUpdatedAt());
        assertTrue(newUser.getCreatedAt().isAfter(beforePersist) || newUser.getCreatedAt().isEqual(beforePersist));
        assertTrue(newUser.getUpdatedAt().isAfter(beforePersist) || newUser.getUpdatedAt().isEqual(beforePersist));
    }

    @Test
    void testPreUpdateCallback() {
        // Arrange
        LocalDateTime originalCreatedAt = user.getCreatedAt();
        LocalDateTime originalUpdatedAt = user.getUpdatedAt();

        // Simulate time passing
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate @PreUpdate
        user.onUpdate();

        // Assert
        assertEquals(originalCreatedAt, user.getCreatedAt()); // Should not change
        assertTrue(user.getUpdatedAt().isAfter(originalUpdatedAt)); // Should be updated
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser");

        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("testuser");

        User user3 = new User();
        user3.setId(2L);
        user3.setUsername("differentuser");

        // Assert
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        // Act
        String userString = user.toString();

        // Assert
        assertNotNull(userString);
        assertTrue(userString.contains("testuser"));
        assertTrue(userString.contains("test@example.com"));
    }

    @Test
    void testUserRoleEnum() {
        // Test all user roles
        assertEquals("DOCTOR", UserRole.DOCTOR.name());
        assertEquals("STAFF", UserRole.STAFF.name());
        assertEquals("ADMIN", UserRole.ADMIN.name());

        // Test role assignment
        user.setRole(UserRole.DOCTOR);
        assertEquals(UserRole.DOCTOR, user.getRole());

        user.setRole(UserRole.STAFF);
        assertEquals(UserRole.STAFF, user.getRole());

        user.setRole(UserRole.ADMIN);
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    void testUserWithNullValues() {
        // Arrange
        User nullUser = new User();

        // Assert - Should handle null values gracefully
        assertNull(nullUser.getId());
        assertNull(nullUser.getUsername());
        assertNull(nullUser.getPassword());
        assertNull(nullUser.getEmail());
        assertNull(nullUser.getRole());
        assertNull(nullUser.getIsActive());

        // UserDetails methods should handle null gracefully
        assertFalse(nullUser.isEnabled()); // isActive is null, should be false
    }
}