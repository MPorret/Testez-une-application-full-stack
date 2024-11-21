package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;



    @Mock
    private UserDetails userDetails;

    private User user1;
    private UserDto userDto1;

    @BeforeEach
    void setUp() {
        user1 = new User("test@gmail.com","Hosni","Brahim",
                "pass@word",false);
        userDto1 = new UserDto(1L,"test@gmail.com","Hosni","Brahim",false,
                "pass@word", LocalDateTime.now(),LocalDateTime.now());

        userDetails = new org.springframework.security.core.userdetails.User(user1.getEmail(),
                user1.getPassword(),new ArrayList<>());
    }

    @Test
    void findById() {
        user1.setId(1L);
        when(userService.findById(1L)).thenReturn(user1);
        when(userMapper.toDto(user1)).thenReturn(userDto1);
        ResponseEntity<?> response = userController.findById("1");
        assertNotNull(user1);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(userDto1,response.getBody());
    }
    @Test
    void findById_shouldReturnNotFoundWhenUserNotFound() {
        when(userService.findById(0L)).thenReturn(null);
        ResponseEntity<?> response = userController.findById("0");
        assertEquals(ResponseEntity.notFound().build(),response);
    }

    @Test
    void findById_shouldReturnBadRequestWhenIdIsInvalid() {
        ResponseEntity<?> response = userController.findById("052llkk");
        assertEquals(ResponseEntity.badRequest().build(),response);
    }

    @Test
    void save() {
        when(userService.findById(1L)).thenReturn(user1);
        assertNotNull(user1);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> response = userController.save("1");
        assertEquals(HttpStatus.OK,response.getStatusCode());

    }
@Test
    void save_shouldReturnNotFoundWhenUserNotFound() {
        when(userService.findById(0L)).thenReturn(null);

        ResponseEntity<?> response = userController.save("0");
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

    }
    @Test
    void save_shouldReturnBadRequestWhenIdIsInvalid() {
        ResponseEntity<?> response = userController.save("054iujuu");
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());

    }
    @Test
    void save_shouldReturnUnauthorizedWhenUserEmailIsInvalid() {
        user1.setEmail("testNotFound@gmail.com");
        when(userService.findById(1L)).thenReturn(user1);
        assertNotNull(user1);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> response = userController.save("1");
        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());

    }
}