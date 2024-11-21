package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("testuser@example.com");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");
        mockUser.setPassword("password");
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        // Arrange
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser@example.com", userDetails.getUsername());
        assertEquals(mockUser.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("nonexistent@example.com"));
    }
}
