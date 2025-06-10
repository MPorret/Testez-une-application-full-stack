package com.openclassrooms.starterjwt.services.it;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SessionServiceTest {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionService sessionService;

    private Session session;

    private User user;

    @BeforeEach
    void init() {
        User user = new User();
        user.setFirstName("User")
                .setLastName("Test")
                .setEmail("user@test.fr")
                .setAdmin(false)
                .setPassword("Test1234");
        this.user = userRepository.save(user);
        Session session = new Session();
        session.setName("Session for tests")
                .setDescription("Description of session")
                .setDate(new Date())
                .setUsers(new ArrayList<>());
        this.session = sessionRepository.save(session);
    }

    @Test
    void testCreate_shouldReturnNewSession(){
        Session session = new Session();
        session.setName("New session for test");
        session.setDescription("Description of the new session");
        session.setDate(new Date());
        Session sessionCreated = sessionService.create(session);
        assertEquals(sessionCreated.getName(), session.getName());
    }

    @Test
    void testDelete_shouldDeleteSession(){
        sessionService.delete(session.getId());
        Session sessionFound = sessionRepository.findById(session.getId()).orElse(null);
        assertNull(sessionFound);
    }

    @Test
    void testFindAll_shouldReturnListOfSessions(){
        List<Session> result = sessionService.findAll();
        assertEquals(result.size(), sessionRepository.findAll().size());
    }

    @Test
    void testGetById_shouldReturnSession() {
        Session sessionFound = sessionService.getById(session.getId());
        assertEquals(sessionFound.getName(), session.getName());
    }

    @Test
    void testGetById_shouldReturnNull() {
        Session sessionFound = sessionService.getById(168L);
        assertNull(sessionFound);
    }

    @Test
    void testUpdate_shouldReturnSessionUpdated() {
        String newName = "Name changed";
        session.setName(newName);
        Session sessionUpdated = sessionService.update(session.getId(), session);
        assertEquals(newName, sessionUpdated.getName());
    }

    @Test
    void testParticipate_shouldUpdateUsersInSession(){
        sessionService.participate(session.getId(), user.getId());
        Session sessionToTest = sessionRepository.getById(session.getId());
        assertTrue(sessionToTest.getUsers().contains(user));
    }

    @Test
    void testParticipate_shouldThrowError_whenSessionNotExist(){
        assertThrows(NotFoundException.class,() -> sessionService.participate(168L, user.getId()));
    }

    @Test
    void testParticipate_shouldThrowError_whenUserNotExist(){
        assertThrows(NotFoundException.class,() -> sessionService.participate(session.getId(), 168L));
    }

    @Test
    void testParticipate_shouldThrowError_whenUserAlreadyParticipate(){
        sessionService.participate(session.getId(), user.getId());
        assertThrows(BadRequestException.class, () -> sessionService.participate(session.getId(), user.getId()));
    }

    @Test
    void testNoLongerParticipate_shouldUpdateUsersInSession(){
        session.getUsers().add(user);
        this.sessionRepository.save(session);
        Session sessionWithUser = sessionRepository.getById(session.getId());
        assertTrue(sessionWithUser.getUsers().contains(user));
        sessionService.noLongerParticipate(session.getId(), user.getId());
        Session sessionToTest = sessionRepository.getById(session.getId());
        assertFalse(sessionToTest.getUsers().contains(user));
    }

    @Test
    void testNoLongerParticipate_shouldThrowError_whenSessionNotExist(){
        assertThrows(NotFoundException.class,() -> sessionService.noLongerParticipate(168L, user.getId()));
    }

    @Test
    void testNoLongerParticipate_shouldThrowError_whenUserAlreadyParticipate(){
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(session.getId(), user.getId()));
    }
}
