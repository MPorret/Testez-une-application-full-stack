package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SessionControllerITTest {
    public static final String YOGA_ADVANCED_CLASS = "Yoga Advanced Class";
    public static final String YOGA_WORKSHOP_DESCRIPTION = "Yoga workshop";
    private Logger logger = LoggerFactory.getLogger(SessionControllerITTest.class);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;
    private Session session;
    private Date date;
    private LocalDateTime now;

    @BeforeEach
    public void setup() {
      //  sessionRepository.deleteAll();
        now = LocalDateTime.now();
        date = new Date();
        teacherRepository.save(new Teacher(1L, "Teacher lastname"
                , "Mohamed", now, now));

        session = Session.builder().id(1L)
                .name(YOGA_ADVANCED_CLASS)
                .date(date)
                .description(YOGA_WORKSHOP_DESCRIPTION)
                .createdAt(now).
                updatedAt(now)
                .build();

        User user = User.builder().id(1L).firstName("Aflouat")
                .lastName("ABDEL WEDOUD")
                .email("aflouat@gmail.com")
                .password("pass@Word01")
                .admin(true)
                .createdAt(now.minusDays(2))
                .updatedAt(now).build();
        userRepository.save(user);

        User participatingUser = User.builder().id(2L).firstName("Hakim")
                .lastName("Legrand")
                .email("h.legrand@gmail.com")
                .password("pass@Word01")
                .admin(true)
                .createdAt(now.minusDays(2))
                .updatedAt(now).build();
        userRepository.save(participatingUser);

        session.setUsers(userRepository.findAll());


        session.setTeacher(teacherRepository.findById(1L).get());

        sessionRepository.save(session);

        Session session2 = Session.builder().id(2L)
                .name(YOGA_ADVANCED_CLASS)
                .date(date)
                .description(YOGA_WORKSHOP_DESCRIPTION)
                .createdAt(now.minusDays(1)).
                updatedAt(now)
                .build();

        sessionRepository.save(session2);

        User notYetParticipatingUser = User.builder().id(3L).firstName("Jemil")
                .lastName("LeJeune")
                .email("j.lejeune@gmail.com")
                .password("pass@Word01")
                .admin(true)
                .createdAt(now.minusDays(2))
                .updatedAt(now).build();
        userRepository.save(notYetParticipatingUser);

    }


    @Test
    @WithMockUser
    public void test_findById_withOK() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/session/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(YOGA_ADVANCED_CLASS)));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session ID", e);

        }
    }

    @Test
    @WithMockUser
    public void test_findById_withNotFound() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/session/99"))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session ID", e);

        }
    }

    @Test
    @WithMockUser
    public void test_findAll_withOK() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/session"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is(YOGA_ADVANCED_CLASS)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is(YOGA_ADVANCED_CLASS)));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session ID", e);

        }
    }

    @Test
    @WithMockUser
    public void test_createSessionWithTeacherAndWithoutUsers_withOK() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                            .contentType("application/json")
                            .content("{\"name\": \"session 1\", \"date\": \"2024-01-01\", \"teacher_id\": 1, " +
                                    "\"users\": null, \"description\": \"my description\"}"))
                    .andExpect(MockMvcResultMatchers.status().isOk()) // ou .isCreated() si le code retour est 201
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("session 1")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.date", containsString("2024-01-01T00:00:00")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.teacher_id", is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("my description")));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session Data", e);
        }
    }

    @Test
    @WithMockUser
    public void test_updateSessionWithInvalidId_returnBadRequest() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/api/session/0")
                            .contentType("application/json")
                            .content("{\"name\": \"session 1\", \"date\": \"2024-01-01\", \"teacher_id\": 1, " +
                                    "\"users\": null, \"description\": \"my description\"}"))
                    .andExpect(MockMvcResultMatchers.status().isOk()); // ou .isCreated() si le code retour est 201
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session Data", e);
        }
    }

    @Test
    @WithMockUser
    public void test_updateSessionWith_returnBadRequest() {
        try {

            mockMvc.perform(MockMvcRequestBuilders.put("/api/session/1")
                            .contentType("application/json")
                            .content("{\"name\": \"session 1 update\", \"date\": \"2024-10-11\", \"teacher_id\": 1, " +
                                    "\"users\": null, \"description\": \"my description update\"}"))
                    .andExpect(MockMvcResultMatchers.status().isOk()) // ou .isCreated() si le code retour est 201
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("session 1 update")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.date", containsString("2024-10-11")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.teacher_id", is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("my description update")));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session Data", e);
        }
    }

    @Test
    @WithMockUser
    public void test_deleteSessionWithNotFound_returnBadRequest() {
        try {

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/0")
                            .contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session Data", e);
        }
    }

    @Test
    @WithMockUser
    public void test_deleteSession_returnOk() {
        try {

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/2")
                            .contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session Data", e);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1ll, 2ll", // Les deux ID sont invalides
            "1ll, 2",   // Seul le premier ID est invalide
            "1, 2ll",   // Seul le second ID est invalide
            "0,1",
            "1,0"

    })
    @WithMockUser
    public void test_participateWithSessionNotFoundOrUserNotFound_returnBadRequest(String sessionId, String userId) {
        try {

            mockMvc.perform(MockMvcRequestBuilders.post("/api/session/" + sessionId + "/participate/" + userId)
                            .contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session Data", e);
        }
    }

    @Test
    @WithMockUser
    public void test_participateWithExistingSessionAndUser_returnOk() {
        try {
System.out.println(":::::::::::::::::participateWithExistingSessionAndUser");
List<Session> sess = sessionRepository.findAll();
            System.out.println("sessions: " + sess);
            List<User> users = userRepository.findAll();
            System.out.println("users: " + users);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/session/4/participate/3"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session Data", e);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1ll, 2ll", // Les deux ID sont invalides
            "1ll, 10",   // Seul le premier ID est invalide
            "1, 2ll",   // Seul le second ID est invalide
            "0,1",
            "1,0",
            "0,10"


    })
    @WithMockUser
    public void test_deleteWithSessionNotFoundOrUserNotFound_returnBadRequest(String sessionId, String userId) {
        try {

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/" + sessionId + "/participate/" + userId)
                            .contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session Data", e);
        }
    }

    @Test
    @WithMockUser
    public void test_deleteWithExistingSessionAndUser_returnOk() {
        try {
            Session foundSession = this.sessionRepository.findById(1L).orElse(null);
            Assertions.assertNotNull(foundSession);

            System.out.println("######################"+foundSession);

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1/participate/1")
                            .contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Session Data", e);
        }
    }
}
