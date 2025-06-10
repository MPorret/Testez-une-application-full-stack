package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.models.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTests {

    @Test
    void equals_shouldReturnTrue_forSameObject() {
        Session session = new Session();
        session.setId(1L);
        assertEquals(session, session);
    }

    @Test
    void equals_shouldReturnFalse_forNull() {
        Session session = new Session();
        session.setId(1L);
        assertNotEquals(session, null);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentClass() {
        Session session = new Session();
        session.setId(1L);
        assertNotEquals(session, "string");
    }

    @Test
    void equals_shouldReturnTrue_forSameId() {
        Session session1 = new Session().setId(1L);
        Session session2 = new Session().setId(1L);
        assertEquals(session1, session2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentId() {
        Session session1 = new Session().setId(1L);
        Session session2 = new Session().setId(2L);
        assertNotEquals(session1, session2);
    }

    @Test
    void equals_shouldReturnFalse_whenOneIdIsNull() {
        Session session1 = new Session().setId(null);
        Session session2 = new Session().setId(2L);
        assertNotEquals(session1, session2);
    }

    @Test
    void hashCode_shouldBeEqual_forSameId() {
        Session session1 = new Session().setId(5L);
        Session session2 = new Session().setId(5L);
        assertEquals(session1.hashCode(), session2.hashCode());
    }

    @Test
    void hashCode_shouldBeDifferent_forDifferentIds() {
        Session session1 = new Session().setId(5L);
        Session session2 = new Session().setId(10L);
        assertNotEquals(session1.hashCode(), session2.hashCode());
    }

    @Test
    void hashCode_shouldNotThrow_whenIdIsNull() {
        Session session = new Session().setId(null);
        assertDoesNotThrow(session::hashCode);
    }

}
