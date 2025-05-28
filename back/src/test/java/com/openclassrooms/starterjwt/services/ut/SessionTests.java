package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.models.Session;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SessionTests {

    @Test
    void equals_shouldReturnTrue_forSameId() {
        Session s1 = new Session().setId(1L);
        Session s2 = new Session().setId(1L);
        assertEquals(s1, s2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentId() {
        Session s1 = new Session().setId(1L);
        Session s2 = new Session().setId(2L);
        assertNotEquals(s1, s2);
    }

    @Test
    void hashCode_shouldBeEqual_forSameId() {
        Session s1 = new Session().setId(5L);
        Session s2 = new Session().setId(5L);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void hashCode_shouldBeDifferent_forDifferentIds() {
        Session s1 = new Session().setId(5L);
        Session s2 = new Session().setId(10L);
        assertNotEquals(s1.hashCode(), s2.hashCode());
    }

}
