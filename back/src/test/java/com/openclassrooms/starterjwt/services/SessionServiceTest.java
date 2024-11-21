package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    SessionRepository sessionRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks // Injecte les mocks dans sessionService
    private SessionService sessionService;

    private Session session;
    private Teacher teacher;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();  // Assurez-vous de définir un objet `Teacher` valide
        teacher.setId(1L);  // Exemple d'ID de l'enseignant

        user1 = new User();
        user1.setId(1L);  // Exemple d'ID utilisateur

        user2 = new User();
        user2.setId(2L);  // Autre utilisateur

        session = Session.builder()
                .name("Yoga Morning Session")
                .date(new Date())  // Date actuelle pour l'exemple
                .description("Une session de yoga matinale pour tous les niveaux")
                .teacher(teacher)
                .users(new ArrayList<>(List.of(user2)))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

    }

    @Test
    void create() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session createdSession = sessionService.create(session);
        Assertions.assertSame(session,createdSession);
        verify(sessionRepository, times(1)).save(session);
    }



    @Test
    void delete() {
        // Exécution de la méthode de suppression
        sessionService.delete(session.getId());
        // Vérification que delete a été appelé une fois
        verify(sessionRepository, times(1)).deleteById(session.getId());
   }

    @Test
    void findAll() {
        Session session2 = Session.builder()
                .name("Yoga Morning Session 2")
                .date(new Date())  // Date actuelle pour l'exemple
                .description("Une session 2  de yoga matinale pour tous les niveaux")
                .teacher(teacher)
                .users(List.of(user1, user2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(sessionRepository.findAll()).thenReturn(List.of(session, session2));
        List<Session> sessions = sessionService.findAll();
        Assertions.assertNotNull(sessions);
        Assertions.assertEquals(2, sessions.size());
        verify(sessionRepository, times(1)).findAll();



    }

    @Test
    void getById() {
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        Session foundSession = sessionService.getById(session.getId());
        Assertions.assertSame(session,foundSession);
        verify(sessionRepository, times(1)).findById(session.getId());
    }

    @Test
    void update() {
        Session session2 = Session.builder()
                .name("Yoga Morning Session updated")
                .date(new Date())  // Date actuelle pour l'exemple
                .description("Une session updated  de yoga matinale pour tous les niveaux")
                .teacher(teacher)
                .users(List.of(user1, user2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(sessionRepository.save(session2)).thenReturn(session2);
        Session uodatedSession = sessionService.update(session.getId(),session2);
        verify(sessionRepository, times(1)).save(uodatedSession);
        Assertions.assertSame(session2,uodatedSession);
        Assertions.assertEquals(session.getId(),uodatedSession.getId());

    }

    @Test
    void participate() {
        session.setId(1L);  // S'assurer que session.getId() retourne 1L

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        sessionService.participate(user1.getId(), session.getId());
        verify(sessionRepository, times(1)).save(session);
    }
    @Test
    void participate_ShouldThrowNotFoundException_WhenSessionOrUserNotFound() {
        // Préparez le contexte pour que la session ne soit pas trouvée
        doReturn(Optional.empty()).when(sessionRepository).findById(1L);
        doReturn(Optional.of(user1)).when(userRepository).findById(1L);

        // Vérifiez que la NotFoundException est levée
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));

        // Test pour l'absence de l'utilisateur
        doReturn(Optional.of(session)).when(sessionRepository).findById(1L);
        doReturn(Optional.empty()).when(userRepository).findById(1L);

        // Vérifiez que la NotFoundException est levée
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }
    @Test
    void participate_ShouldThrowBadRequestException_WhenUserAlreadyParticipatedInSession() {
        // Préparez le contexte pour que la session soit trouvée
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        // l'utilisation participe déjà dans la session
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        session.getUsers().add(user1);

        // Vérifiez que la NotFoundException est levée
        assertThrows(BadRequestException.class, () -> sessionService.participate(session.getId(), user1.getId()));
    }

    @Test
    void noLongerParticipate() {
        session.setId(1L);  // Assurez-vous que session.getId() retourne 1L
        user1.setId(1L);

        // Préparer le contexte : session trouvée, utilisateur présent dans la session
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        // Ajouter user1 dans la liste des utilisateurs de la session pour simuler sa participation
        session.getUsers().add(user1);

        // Appel de la méthode à tester
        sessionService.noLongerParticipate(session.getId(), user1.getId());

        // Vérification que l'utilisateur a bien été retiré et que la session est sauvegardée
        verify(sessionRepository, times(1)).save(session);
        assertFalse(session.getUsers().contains(user1), "L'utilisateur doit être retiré de la session.");


    }

    @Test
    void NoLongerParticipate_ShouldThrowNotFoundException_WhenSessionNotFound() {
        // Préparez le contexte pour que la session ne soit pas trouvée
        doReturn(Optional.empty()).when(sessionRepository).findById(1L);
        // Vérifiez que la NotFoundException est levée
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void noLongerParticipate_ShouldThrowBadRequestException_WhenUserIsNotAlreadyParticipated0000InSession() {
        // Préparez le contexte pour que la session soit trouvée
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        session.setUsers(List.of()); // La liste des utilisateurs est vide ou ne contient pas `user1`


        // Vérifiez que la NotFoundException est levée
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(session.getId(), user1.getId()));
    }


}