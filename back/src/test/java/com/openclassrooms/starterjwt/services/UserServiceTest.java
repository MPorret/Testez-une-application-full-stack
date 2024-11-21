package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    private User user;
    @BeforeEach
    void setUp() {
        user = User.builder().
                id(1L).
                firstName("Mohamed")
                .lastName("Jhon")
                .email("test@user.com")
                .password("password")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


    }

    @Test
    void delete() {
        userService.delete(user.getId());
        verify(userRepository,times(1)).deleteById(1L);

    }

    @Test
    void findById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User found = userService.findById(1L);
        assertNotNull(found);
        assertSame(user, found);
        verify(userRepository,times(1)).findById(1L);


    }

}