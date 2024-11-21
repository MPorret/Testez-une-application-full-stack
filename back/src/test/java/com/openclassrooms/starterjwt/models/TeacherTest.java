package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TeacherTest {
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Smith";
    @InjectMocks
    private Teacher teacher;
    private LocalDateTime NOW = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName(FIRST_NAME);
        teacher.setLastName(LAST_NAME);
        teacher.setCreatedAt(NOW.minusDays(1));
        teacher.setUpdatedAt(NOW);

    }

    @Test
    void testToString() {
        String expected = "Teacher(id=1, lastName="+LAST_NAME+", firstName="+FIRST_NAME
                +", createdAt="+NOW.minusDays(1)+ ", updatedAt="+NOW+")";
        String actual = teacher.toString();
        assertEquals(expected, actual);
    }
    @Test
    void testEquals_sameId_shouldBeEqual() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);

        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);
        assertTrue(teacher1.equals(teacher2));
        assertEquals(teacher1, teacher2, "Two users with the same ID should be equal.");
    }

    @Test
    void testEquals_differentId_shouldNotBeEqual() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        assertFalse(teacher1.equals(teacher2));
        assertNotEquals(teacher1, teacher2, "Two users with the different ID should not be equal.");

    }
    @Test
    void testEquals_withNotInstanceOfTeacher_shouldNotBeEqual() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);

        User user = new User();
        user.setId(1L);
        assertFalse(teacher1.equals(user));

    }


    @Test
    void testHashCode_sameId_shouldHaveSameHashCode() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);

        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);

        assertEquals(teacher1.hashCode(), teacher2.hashCode(), "Two users with the same ID should have same hash code.");
    }

    @Test
    void testHashCode_differentId_shouldHaveDifferentHashCode() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);

        assertNotEquals(teacher1.hashCode(), teacher2.hashCode(), "Two users with the # ID should # hashcode.");
    }
    @Test
    void testEquals_withNull_shouldNotBeEqual() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);

        assertNotEquals(teacher1, null, "Teacher should not be equal to null.");
    }

    @Test
    void testEquals_withSameInstance_shouldBeEqual() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);

        assertEquals(teacher1, teacher1, "Teacher should be equal to itself.");
    }

    @Test
    void testHashCode_withNullId_shouldHandleGracefully() {
        Teacher teacher1 = new Teacher(); // ID not set

        assertDoesNotThrow(() -> teacher1.hashCode(), "Hashcode generation should not throw an exception if ID is null.");
    }
    @Test
    void testEquals_withDifferentType_shouldNotBeEqual() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        String differentTypeObject = "NotATeacher";
        assertFalse(teacher.equals(differentTypeObject), "Teacher should not be equal to an object of a different type.");
    }

    @Test
    void testEquals_withBothNullIds_shouldBeEqual() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();

        assertEquals(teacher1, teacher2, "Two Teacher objects with null IDs should not be considered equal.");
    }

    @Test
    void testHashCode_withNullIds_shouldHaveSameHashCode() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();

        assertEquals(teacher1.hashCode(), teacher2.hashCode(), "Two Teacher objects with null IDs should have the same hash code.");
    }

    @Test
    void testHashCode_withOneNullId_shouldHaveDifferentHashCode() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);

        assertNotEquals(teacher1.hashCode(), teacher2.hashCode(), "Teacher objects with null ID and non-null ID should have different hash codes.");
    }

}