package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;

    private User user;

    @BeforeEach
    void init() {
        user = new User();
        user.setId(2L);
    }

    @Test
    void delete_shouldCallDeleteById(){
        userService.delete(2L);
        verify(userRepository).deleteById(2L);
    }

    @Test
    void findById_shouldCallFindById(){
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(user));
        User result = userService.findById(2L);
        assertEquals(result, user);
        verify(userRepository).findById(2L);
    }

    @Test
    void findById_shouldThrowError_UserNotExists(){
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        User result = userService.findById(2L);
        assertNull(result);
        verify(userRepository).findById(2L);
    }
}
