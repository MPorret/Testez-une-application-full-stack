package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class SessionMapperTests {
    @Autowired
    private SessionMapper sessionMapper;

    private SessionDto sessionDto;
    private Session session;
    private Teacher teacher;

    @BeforeEach
    void init () {
        User user = new User()
                .setId(2L)
                .setEmail("newuser@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("test123")
                .setAdmin(false)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        teacher = new Teacher(
                2L,
                "John",
                "Doe",
                LocalDateTime.now(),
                LocalDateTime.now());

        sessionDto = new SessionDto(
                2L,
                "Name of session",
                new Date(),
                2L,
                "Description of session",
                List.of(user.getId()),
                LocalDateTime.now(),
                LocalDateTime.now());

        session = new Session()
                .setId(2L)
                .setName("Name of session")
                .setDate(new Date())
                .setDescription("Description of session")
                .setTeacher(teacher)
                .setUsers(List.of(user))
                .setCreatedAt(sessionDto.getCreatedAt())
                .setUpdatedAt(sessionDto.getUpdatedAt());
    }

    @Test
    void ToEntity_shouldReturnNull(){
        sessionDto = null;
        assertNull(sessionMapper.toEntity(sessionDto));
    }

    @Test
    void ToDto_shouldReturnNull(){
        session = null;
        assertNull(sessionMapper.toDto(session));
    }

    @Test
    void listToEntity_shouldBeEquals(){
        assertEquals(sessionMapper.toEntity(List.of(sessionDto)), List.of(session));
    }

    @Test
    void listToDto_shouldBeEquals(){
        assertEquals(sessionMapper.toDto(List.of(session)), List.of(sessionDto));
    }


    @Test
    void listToEntity_shouldReturnNull(){
        List<SessionDto> sessionList = null;
        assertNull(sessionMapper.toEntity(sessionList));
    }

    @Test
    void listToDto_shouldReturnNull(){
        List<Session> sessionList = null;
        assertNull(sessionMapper.toDto(sessionList));
    }

    @Test
    void toDto_shouldReturnSessionWithNullTeacherId_teacherNull(){
        session.setTeacher(null);
        assertNull(sessionMapper.toDto(session).getTeacher_id());
    }

    @Test
    void toDto_shouldReturnSessionWithNullTeacherId_teacherIdNull(){
        teacher.setId(null);
        session.setTeacher(teacher);
        assertNull(sessionMapper.toDto(session).getTeacher_id());
    }
}
