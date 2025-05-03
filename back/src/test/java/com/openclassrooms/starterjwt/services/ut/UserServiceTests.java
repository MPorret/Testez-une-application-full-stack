package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
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
    private static final Long userId = 2L;

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;

    private User user;

    @BeforeEach
    void init() {
        user = new User();
        user.setId(userId);
    }

    @Test
    void delete_shouldCallDeleteById(){
        userService.delete(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void findById_shouldCallFindById(){
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        User result = userService.findById(userId);
        assertEquals(result, user);
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_shouldThrowError_UserNotExists(){
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        User result = userService.findById(userId);
        assertNull(result);
        verify(userRepository).findById(userId);
    }
}
