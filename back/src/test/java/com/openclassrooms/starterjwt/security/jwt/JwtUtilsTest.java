package com.openclassrooms.starterjwt.security.jwt;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userPrincipal;

    @InjectMocks
    private JwtUtils jwtUtils;

    private String jwtSecret = "testSecret";
    private int jwtExpirationMs = 3600000; // 1 heure

    @BeforeEach
    void setUp() throws Exception {
        setPrivateField(jwtUtils, "jwtSecret", jwtSecret);
        setPrivateField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    void generateJwtToken_ShouldReturnToken() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUsername()).thenReturn("testUser");

        // Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertNotNull(token);
        assertDoesNotThrow(() -> Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token));
    }

    @Test
    void getUserNameFromJwtToken_ShouldReturnUsername() {
        // Arrange
        String username = "testUser";
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Act
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateJwtToken_ValidToken_ShouldReturnTrue() {
        // Arrange
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateJwtToken_ExpiredToken_ShouldReturnFalse() {
        // Arrange
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(System.currentTimeMillis() - jwtExpirationMs - 1000)) // jeton expiré
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_InvalidSignature_ShouldReturnFalse() {
        // Arrange
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_MalformedToken_ShouldReturnFalse() {
        // Arrange
        String token = "malformed.token.value";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    }
    @Test
    void validateJwtToken_UnsupportedJwtException_ShouldReturnFalse() {
        // Arrange
        String token = "type:";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    }

    // Méthode utilitaire pour définir les champs privés
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
