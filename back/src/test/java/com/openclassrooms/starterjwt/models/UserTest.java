package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserTest {
    public static final String MAIL = "jeanphilippe@renault.fr";
    public static final String LAST_NAME = "DeLaPorte";
    public static final String FIRST_NAME = "Jean-Philippe";
    public static final String PASSWORD = "pass@Word01";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final LocalDateTime UPDATED_AT = LocalDateTime.now().plusDays(1);

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName(FIRST_NAME);
        testUser.setLastName(LAST_NAME);
        testUser.setEmail(MAIL);
        testUser.setPassword(PASSWORD);
        testUser.setCreatedAt(NOW);
        testUser.setUpdatedAt(UPDATED_AT);


    }
    @Test
    void testToString_withEmptyUser_shouldBeEmpty() {
        User testUserEmpty = new User();
        assertEquals("User(id=null, email=null, lastName=null, firstName=null, password=null," +
                " admin=false, createdAt=null, updatedAt=null)", testUserEmpty.toString());
    }
    @Test
    void testToString_witUser_shouldBeNotEmpty() {
        String expectedString = "User(id=1, email="+MAIL+", lastName="+LAST_NAME+", firstName="+FIRST_NAME+", password="+
                PASSWORD+"," + " admin=false, createdAt="+NOW+", updatedAt="+UPDATED_AT+")";
        assertEquals(expectedString, testUser.toString());
    }
    @Test
    void testToString_witUserWithoutDates_shouldBeOK() {
        String expectedString = "User(id=1, email="+MAIL+", lastName="+LAST_NAME+", firstName="+FIRST_NAME+", password="+
                PASSWORD+"," + " admin=false, createdAt=null, updatedAt=null)";
        testUser.setCreatedAt(null);
        testUser.setUpdatedAt(null);
        assertEquals(expectedString, testUser.toString());
    }
    @Test
    void testEquals_sameId_shouldBeEqual() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        assertEquals(user1, user2, "Two users with the same ID should be equal.");
    }

    @Test
    void testEquals_differentId_shouldNotBeEqual() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        assertNotEquals(user1, user2, "Two users with different IDs should not be equal.");
    }

    @Test
    void testHashCode_sameId_shouldHaveSameHashCode() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        assertEquals(user1.hashCode(), user2.hashCode(), "Two users with the same ID should have the same hash code.");
    }

    @Test
    void testHashCode_differentId_shouldHaveDifferentHashCode() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        assertNotEquals(user1.hashCode(), user2.hashCode(), "Two users with different IDs should have different hash codes.");
    }
}