package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SpringBootApplicationTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> SpringBootSecurityJwtApplication.main(new String[] {}));
    }
}
