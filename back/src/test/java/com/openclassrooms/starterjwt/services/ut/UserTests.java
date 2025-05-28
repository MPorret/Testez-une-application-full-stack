package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
    void setter_shouldThrowException_whenLAstNameIsNull() {
        User user = new User();

        assertThrows(NullPointerException.class, () -> {
            user.setLastName(null);
        });
    }

}
