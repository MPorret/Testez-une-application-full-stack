package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
public class UserServiceIT {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User userCreated;

    @BeforeEach
    void init(){
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        userCreated = userRepository.save(user);
    }

    @Test
    void testFindById_shouldReturnUser() {
        User userFound = userService.findById(userCreated.getId());
        assertEquals(userCreated.getId(), userFound.getId());
    }

    @Test
    void testFindById_shouldReturnNull() {
        User userFound = userService.findById(168L);
        assertNull(userFound);
    }

    @Test
    void testDelete_shouldDeleteUser() {
        userService.delete(userCreated.getId());
        User userDeleted = userService.findById(userCreated.getId());
        assertNull(userDeleted);
    }
}
