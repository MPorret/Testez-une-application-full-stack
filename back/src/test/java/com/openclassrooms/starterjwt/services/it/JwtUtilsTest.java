package com.openclassrooms.starterjwt.services.it;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@TestPropertySource(properties = {
        "oc.app.jwtSecret=testSecretKey123456",
        "oc.app.jwtExpirationMs=3600000"
})
public class JwtUtilsTest {
    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void validateJwtToken_shouldReturnFalse_whenMalformedToken() {
        String malformedToken = "malformated.token";
        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenNull() {
        assertFalse(jwtUtils.validateJwtToken(null));
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenEmpty() {
        assertFalse(jwtUtils.validateJwtToken(""));
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenExpired() {
        String expiredToken = Jwts.builder()
                .setSubject("testuser@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000))  // issued 2h ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000))   // expired 1s ago
                .signWith(SignatureAlgorithm.HS512, "testSecretKey123456")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }
}