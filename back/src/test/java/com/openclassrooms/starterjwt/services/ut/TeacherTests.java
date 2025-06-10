package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTests {

    @Test
    void equals_shouldReturnTrue_forSameObject() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        assertEquals(teacher, teacher);
    }

    @Test
    void equals_shouldReturnFalse_forNull() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        assertNotEquals(teacher, null);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentClass() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        assertNotEquals(teacher, "string");
    }

    @Test
    void equals_shouldReturnTrue_forSameId() {
        Teacher teacher1 = new Teacher().setId(1L);
        Teacher teacher2 = new Teacher().setId(1L);
        assertEquals(teacher1, teacher2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentId() {
        Teacher teacher1 = new Teacher().setId(1L);
        Teacher teacher2 = new Teacher().setId(2L);
        assertNotEquals(teacher1, teacher2);
    }

    @Test
    void equals_shouldReturnFalse_whenOneIdIsNull() {
        Teacher teacher1 = new Teacher().setId(null);
        Teacher teacher2 = new Teacher().setId(2L);
        assertNotEquals(teacher1, teacher2);
    }

    @Test
    void hashCode_shouldBeEqual_forSameId() {
        LocalDateTime date = LocalDateTime.now();
        Teacher teacher1 = new Teacher(5L, "Test", "Teacher", date, date);
        Teacher teacher2 = new Teacher(5L, "Test", "Teacher", date, date);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @Test
    void hashCode_shouldBeDifferent_forDifferentIds() {
        LocalDateTime date = LocalDateTime.now();
        Teacher teacher1 = new Teacher(5L, "Test", "Teacher", date, date);
        Teacher teacher2 = new Teacher(10L, "Test", "Teacher", date, date);
        assertNotEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @Test
    void hashCode_shouldNotThrow_whenIdIsNull() {
        Teacher teacher = new Teacher().setId(null);
        assertDoesNotThrow(teacher::hashCode);
    }
}
