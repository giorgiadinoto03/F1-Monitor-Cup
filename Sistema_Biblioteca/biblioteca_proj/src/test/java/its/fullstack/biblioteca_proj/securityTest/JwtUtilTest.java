package its.fullstack.biblioteca_proj.securityTest;

import its.fullstack.biblioteca_proj.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;
    private UserDetails adminUserDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        // Set test values using reflection since they're @Value annotated
        ReflectionTestUtils.setField(jwtUtil, "secret", "testSecretKeyThatIsLongEnoughForHMACSigningAndTestingPurposes");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L); // 24 hours

        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("test@test.com")
                .password("password")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))
                .build();

        adminUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username("admin@test.com")
                .password("password")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();
    }

    @Test
    void generateToken_WhenValidUserDetails_ShouldGenerateToken() {
        // When
        String token = jwtUtil.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts separated by dots
    }

    @Test
    void extractUsername_WhenValidToken_ShouldReturnUsername() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertEquals("test@test.com", extractedUsername);
    }

    @Test
    void extractExpiration_WhenValidToken_ShouldReturnFutureDate() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        Date expiration = jwtUtil.extractExpiration(token);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void extractRole_WhenClientUser_ShouldReturnClientRole() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        String role = jwtUtil.extractRole(token);

        // Then
        assertEquals("ROLE_CLIENT", role);
    }

    @Test
    void extractRole_WhenAdminUser_ShouldReturnAdminRole() {
        // Given
        String token = jwtUtil.generateToken(adminUserDetails);

        // When
        String role = jwtUtil.extractRole(token);

        // Then
        assertEquals("ROLE_ADMIN", role);
    }

    @Test
    void validateToken_WhenValidTokenAndMatchingUser_ShouldReturnTrue() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        Boolean isValid = jwtUtil.validateToken(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void validateToken_WhenValidTokenButDifferentUser_ShouldReturnFalse() {
        // Given
        String token = jwtUtil.generateToken(userDetails);
        UserDetails differentUser = org.springframework.security.core.userdetails.User.builder()
                .username("different@test.com")
                .password("password")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))
                .build();

        // When
        Boolean isValid = jwtUtil.validateToken(token, differentUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    void validateToken_WhenExpiredToken_ShouldReturnFalse() {
        // Given
        ReflectionTestUtils.setField(jwtUtil, "expiration", -1L); // Expired immediately
        String expiredToken = jwtUtil.generateToken(userDetails);

        // Reset expiration for validation
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);

        // When
        Boolean isValid = jwtUtil.validateToken(expiredToken, userDetails);

        // Then
        assertFalse(isValid);
    }

    @Test
    void extractClaim_WhenValidToken_ShouldExtractCustomClaim() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        String subject = jwtUtil.extractClaim(token, claims -> claims.getSubject());

        // Then
        assertEquals("test@test.com", subject);
    }

    @Test
    void generateToken_WhenMultipleRoles_ShouldUseFirstRole() {
        // Given
        UserDetails multiRoleUser = org.springframework.security.core.userdetails.User.builder()
                .username("multirole@test.com")
                .password("password")
                .authorities(Arrays.asList(
                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("ROLE_CLIENT")
                ))
                .build();

        // When
        String token = jwtUtil.generateToken(multiRoleUser);
        String extractedRole = jwtUtil.extractRole(token);

        // Then
        assertEquals("ROLE_ADMIN", extractedRole);
    }

    @Test
    void extractUsername_WhenInvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When & Then
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(invalidToken));
    }

    @Test
    void extractExpiration_WhenInvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When & Then
        assertThrows(Exception.class, () -> jwtUtil.extractExpiration(invalidToken));
    }

    @Test
    void validateToken_WhenInvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When & Then
        assertThrows(Exception.class, () -> jwtUtil.validateToken(invalidToken, userDetails));
    }
}