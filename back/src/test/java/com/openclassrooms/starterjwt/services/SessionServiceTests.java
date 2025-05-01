package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
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
        session.setId(45L);
        session.setUsers(new ArrayList<>());

        user = new User();
        user.setId(2L);
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
        sessionService.delete(45L);
        verify(sessionRepository).deleteById(45L);
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
        when(sessionRepository.findById(2L)).thenReturn(Optional.ofNullable(session));
        Session result = sessionService.getById(2L);
        assertEquals(result, session);
        verify(sessionRepository).findById(2L);
    }

    @Test
    public void getById_shouldThrowError_userNotExists() {
        when(sessionRepository.findById(2L)).thenReturn(Optional.empty());
        Session result = sessionService.getById(2L);
        assertNull(result);
        verify(sessionRepository).findById(2L);
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
        when(sessionRepository.findById(45L)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(user));
        sessionService.participate(45L, 2L);
        assertEquals(session.getUsers(), List.of(user));
        verify(sessionRepository).findById(45L);
        verify(userRepository).findById(2L);
        verify(sessionRepository).save(session);
    }

    @Test
    public void participate_shouldThrowError_userNotExists() {
        when(sessionRepository.findById(45L)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(45L, 2L));
        verify(sessionRepository).findById(45L);
        verify(userRepository).findById(2L);
    }

    @Test
    public void participate_shouldThrowError_sessionNotExists() {
        when(sessionRepository.findById(45L)).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(user));
        assertThrows(NotFoundException.class, () -> sessionService.participate(45L, 2L));
        verify(sessionRepository).findById(45L);
        verify(userRepository).findById(2L);
    }

    @Test
    public void participate_shouldThrowError_userAlreadyParticipate() {
        session.setUsers(List.of(user));
        when(sessionRepository.findById(45L)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(user));
        assertThrows(BadRequestException.class, () -> sessionService.participate(45L, 2L));
        verify(sessionRepository).findById(45L);
        verify(userRepository).findById(2L);
    }

    @Test
    public void noLongerParticipate_shouldDeleteUserAndCallSave() {
        session.setUsers(List.of(user));
        when(sessionRepository.findById(45L)).thenReturn(Optional.ofNullable(session));
        sessionService.noLongerParticipate(45L, 2L);
        assertEquals(session.getUsers(), List.of());
        verify(sessionRepository).findById(45L);
        verify(sessionRepository).save(session);
    }

    @Test
    public void noLongerParticipate_shouldThrowError_sessionNotExists() {
        when(sessionRepository.findById(45L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(45L, 2L));
        verify(sessionRepository).findById(45L);
    }

    @Test
    public void noLongerParticipate_shouldThrowError_userAlreadyNotParticipate() {
        when(sessionRepository.findById(45L)).thenReturn(Optional.ofNullable(session));
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(45L, 2L));
        verify(sessionRepository).findById(45L);
    }

}
