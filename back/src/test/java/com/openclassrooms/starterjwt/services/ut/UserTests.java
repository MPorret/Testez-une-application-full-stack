package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    @Test
    void constructor_shouldThrowException_whenEmailIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new User(null,"Test", "User", "Test1234", false);
        });
    }

    @Test
    void constructor_shouldThrowException_whenLastnameIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new User("test@test.com",null, "User", "Test1234", false);
        });
    }

    @Test
    void constructor_shouldThrowException_whenFirstnameIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new User("test@test.com","Test", null, "Test1234", false);
        });
    }

    @Test
    void constructor_shouldThrowException_whenPasswordIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new User("test@test.com","Test", "User", null, false);
        });
    }

    @Test
    void constructorAllArgs_shouldThrowException_whenEmailIsNull() {
        LocalDateTime date = LocalDateTime.now();
        assertThrows(NullPointerException.class, () -> {
            new User(1L, null,"Test", "User", "Test1234", false, date, date);
        });
    }

    @Test
    void constructorAllArgs_shouldThrowException_whenLastnameIsNull() {
        LocalDateTime date = LocalDateTime.now();
        assertThrows(NullPointerException.class, () -> {
            new User(1L, "test@test.com",null, "User", "Test1234", false, date, date);
        });
    }

    @Test
    void constructorAllArgs_shouldThrowException_whenFirstnameIsNull() {
        LocalDateTime date = LocalDateTime.now();
        assertThrows(NullPointerException.class, () -> {
            new User(1L, "test@test.com","Test", null, "Test1234", false, date, date);
        });
    }

    @Test
    void constructorAllArgs_shouldThrowException_whenPasswordIsNull() {
        LocalDateTime date = LocalDateTime.now();
        assertThrows(NullPointerException.class, () -> {
            new User(1L, "test@test.com","Test", "User", null, false, date, date);
        });
    }

    @Test
    void setter_shouldThrowException_whenPasswordIsNull() {
        User user = new User();

        assertThrows(NullPointerException.class, () -> {
            user.setPassword(null);
        });
    }

    @Test
    void setter_shouldThrowException_whenEmailIsNull() {
        User user = new User();

        assertThrows(NullPointerException.class, () -> {
            user.setEmail(null);
        });
    }

    @Test
    void setter_shouldThrowException_whenFirstNameIsNull() {
        User user = new User();

        assertThrows(NullPointerException.class, () -> {
            user.setFirstName(null);
        });
    }

    @Test
    void setter_shouldThrowException_whenLastNameIsNull() {
        User user = new User();

        assertThrows(NullPointerException.class, () -> {
            user.setLastName(null);
        });
    }

    @Test
    void equals_shouldReturnTrue_forSameObject() {
        User user = new User();
        user.setId(1L);
        assertEquals(user, user);
    }

    @Test
    void equals_shouldReturnFalse_forNull() {
        User user = new User();
        user.setId(1L);
        assertNotEquals(user, null);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentClass() {
        User user = new User();
        user.setId(1L);
        assertNotEquals(user, "string");
    }

    @Test
    void equals_shouldReturnTrue_forSameId() {
        User user1 = new User().setId(1L);
        User user2 = new User().setId(1L);
        assertEquals(user1, user2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentId() {
        User user1 = new User().setId(1L);
        User user2 = new User().setId(2L);
        assertNotEquals(user1, user2);
    }

    @Test
    void equals_shouldReturnFalse_whenOneIdIsNull() {
        User user1 = new User().setId(null);
        User user2 = new User().setId(2L);
        assertNotEquals(user1, user2);
    }

    @Test
    void hashCode_shouldBeEqual_forSameId() {
        LocalDateTime date = LocalDateTime.now();
        User user1 = new User(5L, "test@test.com","Test", "User", "Test1234!", false, date, date);
        User user2 = new User(5L, "test@test.com","Test", "User", "Test1234!", false, date, date);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void hashCode_shouldBeDifferent_forDifferentIds() {
        LocalDateTime date = LocalDateTime.now();
        User user1 = new User(5L, "test@test.com","Test", "User", "Test1234!", false, date, date);
        User user2 = new User(10L, "test@test.com","Test", "User", "Test1234!", false, date, date);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void hashCode_shouldNotThrow_whenIdIsNull() {
        User user = new User().setId(null);
        assertDoesNotThrow(user::hashCode);
    }

}
