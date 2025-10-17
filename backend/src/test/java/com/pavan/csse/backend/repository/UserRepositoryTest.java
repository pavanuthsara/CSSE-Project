package com.pavan.csse.backend.repository;

import com.pavan.csse.backend.model.User;
import com.pavan.csse.backend.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setPhoneNumber("+1234567890");
        testUser.setRole(UserRole.DOCTOR);
        testUser.setIsActive(true);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testSaveUser() {
        // Act
        User savedUser = userRepository.save(testUser);

        // Assert
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals(UserRole.DOCTOR, savedUser.getRole());
        assertTrue(savedUser.getIsActive());
    }

    @Test
    void testFindByUsername() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByUsernameNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindByEmail() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindByRole() {
        // Arrange
        User doctorUser = new User();
        doctorUser.setUsername("doctor");
        doctorUser.setPassword("password");
        doctorUser.setEmail("doctor@example.com");
        doctorUser.setFirstName("Dr.");
        doctorUser.setLastName("Doctor");
        doctorUser.setRole(UserRole.DOCTOR);
        doctorUser.setIsActive(true);
        doctorUser.setCreatedAt(LocalDateTime.now());
        doctorUser.setUpdatedAt(LocalDateTime.now());

        User staffUser = new User();
        staffUser.setUsername("staff");
        staffUser.setPassword("password");
        staffUser.setEmail("staff@example.com");
        staffUser.setFirstName("Staff");
        staffUser.setLastName("Member");
        staffUser.setRole(UserRole.STAFF);
        staffUser.setIsActive(true);
        staffUser.setCreatedAt(LocalDateTime.now());
        staffUser.setUpdatedAt(LocalDateTime.now());

        entityManager.persistAndFlush(doctorUser);
        entityManager.persistAndFlush(staffUser);

        // Act
        List<User> doctors = userRepository.findByRole(UserRole.DOCTOR);
        List<User> staff = userRepository.findByRole(UserRole.STAFF);

        // Assert
        assertEquals(1, doctors.size());
        assertEquals("doctor", doctors.get(0).getUsername());
        assertEquals(1, staff.size());
        assertEquals("staff", staff.get(0).getUsername());
    }

    @Test
    void testFindByIsActiveTrue() {
        // Arrange
        User activeUser = new User();
        activeUser.setUsername("active");
        activeUser.setPassword("password");
        activeUser.setEmail("active@example.com");
        activeUser.setFirstName("Active");
        activeUser.setLastName("User");
        activeUser.setRole(UserRole.DOCTOR);
        activeUser.setIsActive(true);
        activeUser.setCreatedAt(LocalDateTime.now());
        activeUser.setUpdatedAt(LocalDateTime.now());

        User inactiveUser = new User();
        inactiveUser.setUsername("inactive");
        inactiveUser.setPassword("password");
        inactiveUser.setEmail("inactive@example.com");
        inactiveUser.setFirstName("Inactive");
        inactiveUser.setLastName("User");
        inactiveUser.setRole(UserRole.DOCTOR);
        inactiveUser.setIsActive(false);
        inactiveUser.setCreatedAt(LocalDateTime.now());
        inactiveUser.setUpdatedAt(LocalDateTime.now());

        entityManager.persistAndFlush(activeUser);
        entityManager.persistAndFlush(inactiveUser);

        // Act
        List<User> activeUsers = userRepository.findByIsActiveTrue();

        // Assert
        assertEquals(1, activeUsers.size());
        assertEquals("active", activeUsers.get(0).getUsername());
        assertTrue(activeUsers.get(0).getIsActive());
    }

    @Test
    void testExistsByUsername() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        boolean exists = userRepository.existsByUsername("testuser");
        boolean notExists = userRepository.existsByUsername("nonexistent");

        // Assert
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void testExistsByEmail() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        boolean exists = userRepository.existsByEmail("test@example.com");
        boolean notExists = userRepository.existsByEmail("nonexistent@example.com");

        // Assert
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void testUsernameUniqueConstraint() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        User duplicateUser = new User();
        duplicateUser.setUsername("testuser"); // Same username
        duplicateUser.setPassword("password");
        duplicateUser.setEmail("different@example.com");
        duplicateUser.setFirstName("Different");
        duplicateUser.setLastName("User");
        duplicateUser.setRole(UserRole.STAFF);
        duplicateUser.setIsActive(true);
        duplicateUser.setCreatedAt(LocalDateTime.now());
        duplicateUser.setUpdatedAt(LocalDateTime.now());

        // Act & Assert
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(duplicateUser);
        });
    }

    @Test
    void testUpdateUser() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);
        Long userId = savedUser.getId();

        // Act
        savedUser.setFirstName("Updated");
        savedUser.setLastName("Name");
        savedUser.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertEquals(userId, updatedUser.getId());
        assertEquals("Updated", updatedUser.getFirstName());
        assertEquals("Name", updatedUser.getLastName());
        assertEquals("testuser", updatedUser.getUsername()); // Should remain same
    }

    @Test
    void testDeleteUser() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);
        Long userId = savedUser.getId();

        // Act
        userRepository.deleteById(userId);

        // Assert
        Optional<User> deletedUser = userRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }
}