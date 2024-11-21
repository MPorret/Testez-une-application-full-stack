package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .firstName("Test")
                .lastName("User")
                .admin(true)
                .password("securePassword")
                .build();
    }

    @Test
    void testGetAuthorities_shouldReturnEmptyCollection() {
        assertTrue(userDetails.getAuthorities().isEmpty(), "Expected authorities to be empty");
    }

    @Test
    void testIsAccountNonExpired_shouldReturnTrue() {
        assertTrue(userDetails.isAccountNonExpired(), "Expected account to be non-expired");
    }

    @Test
    void testIsAccountNonLocked_shouldReturnTrue() {
        assertTrue(userDetails.isAccountNonLocked(), "Expected account to be non-locked");
    }

    @Test
    void testIsCredentialsNonExpired_shouldReturnTrue() {
        assertTrue(userDetails.isCredentialsNonExpired(), "Expected credentials to be non-expired");
    }

    @Test
    void testIsEnabled_shouldReturnTrue() {
        assertTrue(userDetails.isEnabled(), "Expected user to be enabled");
    }

    @Test
    void testEquals_sameId_shouldBeEqual() {
        UserDetailsImpl otherUser = UserDetailsImpl.builder()
                .id(1L)
                .username("otherUser")
                .firstName("Other")
                .lastName("User")
                .admin(false)
                .password("otherPassword")
                .build();

        assertEquals(userDetails, otherUser, "Users with the same ID should be equal");
    }

    @Test
    void testEquals_differentId_shouldNotBeEqual() {
        UserDetailsImpl otherUser = UserDetailsImpl.builder()
                .id(2L)
                .username("otherUser")
                .firstName("Other")
                .lastName("User")
                .admin(false)
                .password("otherPassword")
                .build();

        assertNotEquals(userDetails, otherUser, "Users with different IDs should not be equal");
    }

    @Test
    void testEquals_withNull_shouldNotBeEqual() {
        assertNotEquals(userDetails, null, "User should not be equal to null");
    }

    @Test
    void testEquals_withDifferentType_shouldNotBeEqual() {
        String otherObject = "Not a UserDetailsImpl";
        assertNotEquals(userDetails, otherObject, "User should not be equal to an object of a different type");
    }
}
