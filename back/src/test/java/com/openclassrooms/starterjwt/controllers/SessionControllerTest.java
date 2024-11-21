package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SessionControllerTest {
    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private Session mockSession;
    private SessionDto mockSessionDto;
    @BeforeEach
    public void setUp() {
        // Initialisation des objets mocks pour une session
        mockSession = new Session();
        mockSession.setId(1L);
        mockSession.setName("Test Session");

        mockSessionDto = new SessionDto();
        mockSessionDto.setName("Test Session");

    }



    @Test
    public void testFindById_Success() {
        when(sessionService.getById(1L)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);


        // Exécution de la méthode `findById` avec un ID valide
        ResponseEntity<?> responseEntity = sessionController.findById("1");
        assertNotNull(mockSession);
        // Vérification de la réponse
        assertEquals(mockSessionDto, responseEntity.getBody());
    }
    @Test
    public void testFindByIdWithSessionNotFound() {
        mockSession = null;
        when(sessionService.getById(2L)).thenReturn(mockSession);
        ResponseEntity<?> responseEntity = sessionController.findById("2");
        assertNull(mockSession);
        assertEquals(ResponseEntity.notFound().build(), responseEntity);

    }
    @Test
    public void shouldReturnBadRequestOnInvalidId() {
        ResponseEntity<?> responseEntity = sessionController.findById("2ll");

        assertEquals(ResponseEntity.badRequest().build(), responseEntity);

    }

    @Test
    void findAll() {
        when(sessionService.findAll()).thenReturn(List.of(mockSession));
        when(sessionMapper.toDto(List.of(mockSession))).thenReturn(List.of(mockSessionDto));
        ResponseEntity<?> responseEntity = sessionController.findAll();

        assertEquals(List.of(mockSessionDto), responseEntity.getBody());
    }

    @Test
    void create() {

        when(sessionService.create(mockSession)).thenReturn(mockSession);
        when(sessionMapper.toEntity(mockSessionDto)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);
        ResponseEntity<?> responseEntity = sessionController.create(mockSessionDto);
        assertEquals(mockSessionDto, responseEntity.getBody());
    }

    @Test
    void update() {
        SessionDto updatedSessionDto = new SessionDto();
        updatedSessionDto.setName("updated session 1");
        Session updatedSession = new Session();
        updatedSession.setName("updated session 2");
        when(sessionMapper.toEntity(updatedSessionDto)).thenReturn(updatedSession);
        updatedSession.setId(1L);
        updatedSession.setUpdatedAt(LocalDateTime.now());
        when(sessionService.update(1L, updatedSession)).thenReturn(updatedSession);

        when(sessionMapper.toDto(updatedSession)).thenReturn(updatedSessionDto);
        ResponseEntity<?> responseEntity = sessionController.update("1", updatedSessionDto);
        assertEquals(updatedSessionDto, responseEntity.getBody());

    }
    @Test
    void testUpdateWithInvalidIdFormat() {
        SessionDto updatedSessionDto = new SessionDto();
        updatedSessionDto.setName("updated session 1");

        // Exécution de la méthode avec un ID mal formaté
        ResponseEntity<?> responseEntity = sessionController.update("1ll", updatedSessionDto);

        // Vérification que la réponse est un "Bad Request"
        assertEquals(ResponseEntity.badRequest().build(), responseEntity);    }

    @Test
    void shouldDeleteSessionWhenSessionExists() {
        when(sessionService.getById(1L)).thenReturn(mockSession);
        ResponseEntity<?> responseEntity = sessionController.save("1");
        // Vérifications des assertions
        assertEquals(ResponseEntity.ok().build(), responseEntity);
        verify(sessionService).delete(1L);
    }
    @Test
    void shouldReturnNotFoundWhenSessionDoesNotExists() {
        when(sessionService.getById(0L)).thenReturn(null);
        ResponseEntity<?> responseEntity = sessionController.save("0");
        // Vérifications des assertions
        assertEquals(ResponseEntity.notFound().build(), responseEntity);
    }
    @Test
    void shouldReturnBadRequestWhenIdIsInvalid() {


        // Exécution de la méthode avec un ID mal formaté
        ResponseEntity<?> responseEntity = sessionController.save("1ll");

        // Vérification que la réponse est un "Bad Request"
        assertEquals(ResponseEntity.badRequest().build(), responseEntity);

    }
    @ParameterizedTest
    @CsvSource({
            "1ll, 2ll", // Les deux ID sont invalides
            "1ll, 2",   // Seul le premier ID est invalide
            "1, 2ll"    // Seul le second ID est invalide

    })
    void participate_shouldReturnBadRequestWhenIdIsInvalid(String sessionId, String userId) {
        ResponseEntity<?> responseEntity = sessionController.participate(sessionId, userId);
        assertEquals(ResponseEntity.badRequest().build(), responseEntity);
    }
    @Test
    void participate_shouldReturnSuccessWhenIdIsValid() {

        ResponseEntity<?> responseEntity = sessionController.participate("1", "2");
        assertEquals(ResponseEntity.ok().build(), responseEntity);

    }

    @Test
    void noLongerParticipate() {

        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate("1", "2");
        assertEquals(ResponseEntity.ok().build(), responseEntity);
    }
    @ParameterizedTest
    @CsvSource({
            "1ll, 2ll", // Les deux ID sont invalides
            "1ll, 2",   // Seul le premier ID est invalide
            "1, 2ll"    // Seul le second ID est invalide

    })
    void noLongerParticipate_shouldReturnBadRequestWhenIdIsInvalid(String sessionId, String userId) {
        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(sessionId, userId);
        assertEquals(ResponseEntity.badRequest().build(), responseEntity);
    }
}