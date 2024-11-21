package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionMapperImplTest {
    @InjectMocks
    private SessionMapperImpl sessionMapper;
    @Mock
    private Teacher teacher;
    @Mock
    private List<User> users;
    @Mock
    private TeacherService teacherService;
    private Session session;
    private Session secondSession;
    private SessionDto sessionDto;
    private SessionDto secondSessionDto;
    private LocalDateTime now = LocalDateTime.now();
    private Date dateNow = new Date();
    List<Long> userIds = new ArrayList<>();
    @Mock
    private UserService userService;
    User user ;
    @BeforeEach
    void setUp() {
        userIds.add(1L);
        teacher = new Teacher();
        teacher.setId(1L);

        sessionDto = new SessionDto(1L,"nouvelle session",dateNow,1L,
                "description de la session",null,now.minusDays(2),now);
        secondSessionDto = new SessionDto(2L,"nouvelle session 2",dateNow,1L,
                "description de la session",null,now.minusDays(1),now);
        users = new ArrayList<>();
        user = new User();
        user.setId(1L);
        user.setFirstName("nouvelle user");
        users.add(user);


         session = new Session(1L, "nouvelle session", dateNow, "description de la session", teacher, users, now.minusDays(2), now);

         secondSession = new Session(2L, "nouvelle session 2", dateNow, "description de la session", null, null, now.minusDays(1), now);

    }

    @Test
    void toEntity_shouldReturnNullWhenSessionDtoIsNull() {
        sessionDto = null;
        Session s = sessionMapper.toEntity(sessionDto);
        assertNull(s);

    }
    @Test
    void toEntity_shouldReturnSessionWhenSessionDtoIsNotNullAndTeacherIsNotNull() {

        Session expectedSession = new Session(1L,"nouvelle session",dateNow,
                "description de la session",teacher,users,now.minusDays(2),now);

        when(teacherService.findById(1L)).thenReturn(teacher);
        Session actualSession = sessionMapper.toEntity(sessionDto);
        assertNotNull(sessionDto.getTeacher_id());
        assertEquals(expectedSession,actualSession);

    }
    @Test
    void toEntity_shouldReturnSessionWhenSessionDtoIsNotNullAndTeacherIsNull() {

        Session expectedSession = new Session(1L,"nouvelle session",dateNow,
                "description de la session",null,users,now.minusDays(2),now);
        sessionDto.setTeacher_id(null);

        Session actualSession = sessionMapper.toEntity(sessionDto);
        assertNull(sessionDto.getTeacher_id());
        assertEquals(expectedSession,actualSession);

    }
    @Test
    void toEntity_shouldReturnSessionWhenSessionDtoIsNotNullAndTeacherIsNullAndWithoutUsers() {

        Session expectedSession = new Session(1L,"nouvelle session",dateNow,
                "description de la session",null,null,now.minusDays(2),now);
        sessionDto.setTeacher_id(null);
        expectedSession.setUsers(new ArrayList<User>());

        Session actualSession = sessionMapper.toEntity(sessionDto);
        assertNull(sessionDto.getTeacher_id());
        assertEquals(0,actualSession.getUsers().size());
        assertEquals(expectedSession,actualSession);

    }

    @Test
    void toDto() {
        SessionDto expectedSessionDto = new SessionDto(1L,"nouvelle session",dateNow,
                1L,"description de la session",userIds,now.minusDays(2),now);

       // when(teacherService.findById(1L)).thenReturn(teacher);
        SessionDto actualSessionDto = sessionMapper.toDto(session);

        assertEquals(expectedSessionDto,actualSessionDto);
    }
    @Test
    void toDto_withoutIdTeacher() {
        SessionDto expectedSessionDto = new SessionDto(1L,"nouvelle session",dateNow,
                null,"description de la session",userIds,now.minusDays(2),now);

        session = new Session(1L, "nouvelle session", dateNow, "description de la session", null, users, now.minusDays(2), now);

        SessionDto actualSessionDto = sessionMapper.toDto(session);

        assertEquals(expectedSessionDto,actualSessionDto);
        assertNull(actualSessionDto.getTeacher_id());
    }
    @Test
    void toDto_withSessionIsNull() {
           Session sessionNull=null;
           SessionDto expectedSessionDtoNull = null;

        SessionDto actualSessionDto = sessionMapper.toDto(sessionNull);

        assertNull(actualSessionDto);

    }
    @Test
    void toDto_withoutUsers() {
        SessionDto expectedSessionDto = new SessionDto(1L,"nouvelle session",dateNow,
                1L,"description de la session",userIds,now.minusDays(2),now);

        // when(teacherService.findById(1L)).thenReturn(teacher);
        SessionDto actualSessionDto = sessionMapper.toDto(session);

        assertEquals(expectedSessionDto,actualSessionDto);
    }

    @Test
    void testToEntity() {


        Session actualSession = sessionMapper.toEntity(sessionDto);

        assertEquals(session,actualSession);

    }
    @Test
    void testToEntity_withoutSessionDtoList() {
        List<SessionDto> sessionDtoList=null;
        List<Session> sessions = new ArrayList<>();


        List<Session> actualSessionList = sessionMapper.toEntity(sessionDtoList);

        assertNull(actualSessionList);

    }
    @Test
    void testToEntity_withSessionDtoList() {
        List<SessionDto> sessionDtoList=new ArrayList<>();
        sessionDtoList.add(sessionDto);
        sessionDtoList.add(secondSessionDto);
        List<Session> expectedSessions = new ArrayList<>();
        expectedSessions.add(session);
        expectedSessions.add(secondSession);


        List<Session> actualSessionList = sessionMapper.toEntity(sessionDtoList);
        assertEquals(expectedSessions,actualSessionList);

    }
    @Test
    void testToDto_withoutSessionList() {
        List<Session> sessionList=null;
        List<SessionDto> sessionDtoList = new ArrayList<>();


        List<SessionDto> actualSessionDtoList = sessionMapper.toDto(sessionList);

        assertNull(actualSessionDtoList);

    }
    @Test
    void testToDto_withSessionList() {
        sessionDto.setUsers(userIds);
        secondSessionDto.setUsers(new ArrayList<Long>());
        secondSessionDto.setTeacher_id(null);
        List<Session> sessionList=new ArrayList<>();
        sessionList.add(session);
        sessionList.add(secondSession);
        List<SessionDto> expectedSessionDtos = new ArrayList<>();
        expectedSessionDtos.add(sessionDto);
        expectedSessionDtos.add(secondSessionDto);


        List<SessionDto> actualSessionDtoList = sessionMapper.toDto(sessionList);
        assertEquals(expectedSessionDtos,actualSessionDtoList);

    }

    @Test
    void testToDto() {
        SessionDto actualSessionDto = sessionMapper.toDto(session);
        sessionDto.setUsers(userIds);
        assertEquals(sessionDto,actualSessionDto);
    }
    @Test
    void toDto_shouldHandleSessionWithoutUsers() {
        List<User> nullUserList = null;
        session.setUsers(nullUserList);
        SessionDto actualSessionDto = sessionMapper.toDto(session);
        //check if actualSessionDto is empty list
        assertEquals(0, actualSessionDto.getUsers().size());
    }


    @Test
    void toEntity_shouldHandleExistingAndNonExistingUserIds() {
        // Créez un utilisateur existant et un identifiant d'utilisateur inexistant
        User existingUser = new User();
        existingUser.setId(1L);

        Long nonExistingUserId = 99L;
        List<Long> usersList = Arrays.asList(1L, nonExistingUserId);

        // Configurez sessionDto avec cette liste d'identifiants
        sessionDto.setUsers(usersList);

        // Mock `userService.findById` pour retourner uniquement l'utilisateur existant
        when(userService.findById(1L)).thenReturn(existingUser);
        when(userService.findById(99L)).thenReturn(null);

        // Conversion du DTO en entité
        Session actualSession = sessionMapper.toEntity(sessionDto);

        // Vérifiez que seuls les utilisateurs existants sont ajoutés dans la liste d'utilisateurs de la session
        assertEquals(2, actualSession.getUsers().size());
        assertEquals(existingUser, actualSession.getUsers().get(0));
        //TODO code de la methode a reprendre avec .filter(Objects::nonNull)
        assertNull(actualSession.getUsers().get(1));
    }

}