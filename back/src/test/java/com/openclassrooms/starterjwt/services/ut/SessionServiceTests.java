package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTests {
    private static final Long userId = 2L;
    private static final Long sessionId = 45L;

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void init() {
        session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());

        user = new User();
        user.setId(userId);
    }

    @Test
    public void create_shouldCallSave() {
        when(sessionRepository.save(session)).thenReturn(session);
        Session result = sessionService.create(session);
        assertEquals(result, session);
        verify(sessionRepository).save(session);
    }

    @Test
    public void delete_shouldCallDeleteById() {
        sessionService.delete(sessionId);
        verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    public void findAll_shouldCallFindAll() {
        List<Session> sessions = List.of(new Session(), new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);
        List<Session> result = sessionService.findAll();
        assertEquals(result, sessions);
        verify(sessionRepository).findAll();
    }

    @Test
    public void getById_shouldCallFindById() {
        when(sessionRepository.findById(userId)).thenReturn(Optional.ofNullable(session));
        Session result = sessionService.getById(userId);
        assertEquals(result, session);
        verify(sessionRepository).findById(userId);
    }

    @Test
    public void getById_shouldThrowError_userNotExists() {
        when(sessionRepository.findById(userId)).thenReturn(Optional.empty());
        Session result = sessionService.getById(userId);
        assertNull(result);
        verify(sessionRepository).findById(userId);
    }

    @Test
    public void update_shouldSetIdAndCallSave() {
        when(sessionRepository.save(session)).thenReturn(session);
        sessionService.update(44L, session);
        assertEquals(44L, session.getId());
        verify(sessionRepository).save(session);
    }

    @Test
    public void participate_shouldAddUserAtSessionAndCallSave() {
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        sessionService.participate(sessionId, userId);
        assertEquals(session.getUsers(), List.of(user));
        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository).save(session);
    }

    @Test
    public void participate_shouldThrowError_userNotExists() {
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
    }

    @Test
    public void participate_shouldThrowError_sessionNotExists() {
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
    }

    @Test
    public void participate_shouldThrowError_userAlreadyParticipate() {
        session.setUsers(List.of(user));
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
    }

    @Test
    public void noLongerParticipate_shouldDeleteUserAndCallSave() {
        session.setUsers(List.of(user));
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.ofNullable(session));
        sessionService.noLongerParticipate(sessionId, userId);
        assertEquals(session.getUsers(), List.of());
        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository).save(session);
    }

    @Test
    public void noLongerParticipate_shouldThrowError_sessionNotExists() {
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
        verify(sessionRepository).findById(sessionId);
    }

    @Test
    public void noLongerParticipate_shouldThrowError_userAlreadyNotParticipate() {
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.ofNullable(session));
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
        verify(sessionRepository).findById(sessionId);
    }

}
