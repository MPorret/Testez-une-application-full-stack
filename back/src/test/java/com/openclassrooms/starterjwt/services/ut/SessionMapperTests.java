package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTests {
    @Mock
    private UserService userService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private SessionMapperImpl sessionMapper = new SessionMapperImpl();

    private SessionDto sessionDto;
    private Session session;
    private Teacher teacher;
    private User user;

    @BeforeEach
    void init () {
        user = new User()
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
        when(userService.findById(2L)).thenReturn(user);
        when(teacherService.findById(2L)).thenReturn(teacher);
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

    @Test
    void toEntity_shouldReturnSessionWithNullTeacherId_teacherIdNull(){
        sessionDto.setTeacher_id(null);
        assertNull(sessionMapper.toEntity(sessionDto).getTeacher());
    }
}
