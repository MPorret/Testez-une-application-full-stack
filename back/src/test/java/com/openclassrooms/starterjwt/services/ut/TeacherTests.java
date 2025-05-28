package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class TeacherTests {

    @Test
    void equals_shouldReturnTrue_forSameId() {
        Teacher s1 = new Teacher().setId(1L);
        Teacher s2 = new Teacher().setId(1L);
        assertEquals(s1, s2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentId() {
        Teacher s1 = new Teacher().setId(1L);
        Teacher s2 = new Teacher().setId(2L);
        assertNotEquals(s1, s2);
    }

    @Test
    void hashCode_shouldBeEqual_forSameId() {
        Teacher s1 = new Teacher().setId(5L);
        Teacher s2 = new Teacher().setId(5L);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void hashCode_shouldBeDifferent_forDifferentIds() {
        Teacher s1 = new Teacher().setId(5L);
        Teacher s2 = new Teacher().setId(10L);
        assertNotEquals(s1.hashCode(), s2.hashCode());
    }

}
