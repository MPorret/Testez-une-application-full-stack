package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserMapperTests {
    @InjectMocks
    private UserMapperImpl userMapper = new UserMapperImpl();

    private UserDto userDto;
    private User user;

    @BeforeEach
    void init () {
        userDto = new UserDto(
                2L,
                "newuser@example.com",
                "John",
                "Doe",
                false,
                "test123",
                LocalDateTime.now(),
                LocalDateTime.now());

        user = new User(
                2L,
                "newuser@example.com",
                "John",
                "Doe",
                "test123",
                false,
                userDto.getCreatedAt(),
                userDto.getUpdatedAt());
    }

    @Test
    void ToEntity_shouldBeEquals(){
        assertEquals(userMapper.toEntity(userDto), user);
    }

    @Test
    void ToEntity_shouldReturnNull(){
        UserDto user = null;
        assertNull(userMapper.toEntity(user));
    }

    @Test
    void ToDto_shouldReturnNull(){
        User user = null;
        assertNull(userMapper.toDto(user));
    }

    @Test
    void listToEntity_shouldBeEquals(){
        assertEquals(userMapper.toEntity(List.of(userDto)), List.of(user));
    }

    @Test
    void listToDto_shouldBeEquals(){
        assertEquals(userMapper.toDto(List.of(user)), List.of(userDto));
    }


    @Test
    void listToEntity_shouldReturnNull(){
        List<UserDto> userList = null;
        assertNull(userMapper.toEntity(userList));
    }

    @Test
    void listToDto_shouldReturnNull(){
        List<User> userList = null;
        assertNull(userMapper.toDto(userList));
    }
}
