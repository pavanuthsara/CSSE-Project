package com.pavan.csse.backend.util;

import com.pavan.csse.backend.model.User;
import com.pavan.csse.backend.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private User testUser;
    private String testSecret = "myTestSecretKey123456789012345678901234567890";
    private Long testExpiration = 3600000L; // 1 hour

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", testExpiration);

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
    void testGenerateToken() {
        // Act
        String token = jwtUtil.generateToken(testUser);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verify token structure (JWT has 3 parts separated by dots)
        String[] tokenParts = token.split("\\.");
        assertEquals(3, tokenParts.length);
    }

    @Test
    void testExtractUsername() {
        // Arrange
        String token = jwtUtil.generateToken(testUser);

        // Act
        String extractedUsername = jwtUtil.extractUsername(token);

        // Assert
        assertEquals(testUser.getUsername(), extractedUsername);
    }

    @Test
    void testExtractExpiration() {
        // Arrange
        String token = jwtUtil.generateToken(testUser);

        // Act
        Date expiration = jwtUtil.extractExpiration(token);

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testValidateTokenSuccess() {
        // Arrange
        String token = jwtUtil.generateToken(testUser);

        // Act
        Boolean isValid = jwtUtil.validateToken(token, testUser);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenWithWrongUser() {
        // Arrange
        String token = jwtUtil.generateToken(testUser);
        
        User differentUser = new User();
        differentUser.setUsername("differentuser");
        differentUser.setIsActive(true);

        // Act
        Boolean isValid = jwtUtil.validateToken(token, differentUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateTokenOnly() {
        // Arrange
        String token = jwtUtil.generateToken(testUser);

        // Act
        Boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testValidateInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        Boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateExpiredToken() throws InterruptedException {
        // Arrange - Create JwtUtil with very short expiration
        JwtUtil shortExpirationJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(shortExpirationJwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(shortExpirationJwtUtil, "expiration", 1L); // 1 ms
        
        String token = shortExpirationJwtUtil.generateToken(testUser);
        
        // Wait for token to expire
        Thread.sleep(10);

        // Act
        Boolean isValid = shortExpirationJwtUtil.validateToken(token, testUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testExtractClaim() {
        // Arrange
        String token = jwtUtil.generateToken(testUser);

        // Act
        String subject = jwtUtil.extractClaim(token, Claims::getSubject);

        // Assert
        assertEquals(testUser.getUsername(), subject);
    }

    @Test
    void testTokenContainsCorrectClaims() {
        // Arrange
        String token = jwtUtil.generateToken(testUser);
        
        // Extract claims manually to verify
        Key key = Keys.hmacShaKeyFor(testSecret.getBytes());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Assert
        assertEquals(testUser.getUsername(), claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }

    @Test
    void testTokenSignedWithCorrectSecret() {
        // Arrange
        String token = jwtUtil.generateToken(testUser);
        
        // Act - Try to parse with correct secret
        Key correctKey = Keys.hmacShaKeyFor(testSecret.getBytes());
        
        // Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            Jwts.parserBuilder()
                .setSigningKey(correctKey)
                .build()
                .parseClaimsJws(token);
        });
    }

    @Test
    void testTokenRejectedWithWrongSecret() {
        // Arrange
        String token = jwtUtil.generateToken(testUser);
        String wrongSecret = "wrongSecretKey123456789012345678901234567890";
        
        // Act & Assert - Should throw exception with wrong secret
        Key wrongKey = Keys.hmacShaKeyFor(wrongSecret.getBytes());
        
        assertThrows(Exception.class, () -> {
            Jwts.parserBuilder()
                .setSigningKey(wrongKey)
                .build()
                .parseClaimsJws(token);
        });
    }
}