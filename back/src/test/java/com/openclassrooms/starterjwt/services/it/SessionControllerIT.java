package com.openclassrooms.starterjwt.services.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SessionControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SessionMapper sessionMapper;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;

    private Session session;
    private Teacher teacher;
    private String token;
    private User user;

    @BeforeEach
    void initToken(){
        User newUser = new User()
                .setFirstName("User")
                .setLastName("For test")
                .setAdmin(false)
                .setEmail("user@test.com")
                .setPassword(passwordEncoder.encode("test1234"));

        user = userRepository.saveAndFlush(newUser);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), "test1234"));
        token = jwtUtils.generateJwtToken(authentication);
    }

    @BeforeEach
    void init(){
        Session newSession = new Session();
        newSession.setName("New session")
                .setDate(new Date())
                .setDescription("Description of the session")
                .setUsers(new ArrayList<User>());
        Teacher newTeacher = new Teacher();
        newTeacher.setLastName("Test")
                .setFirstName("Teacher");
        teacher = teacherRepository.save(newTeacher);
        newSession.setTeacher(teacher);
        session = sessionRepository.save(newSession);
    }

    @Test
    void findById_shouldReturnSessionDetails_sessionExist() throws Exception {
        mockMvc.perform(get("/api/session/" + session.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.name")).value(session.getName()))
                .andExpect((jsonPath("$.description")).value(session.getDescription()));
    }

    @Test
    void findById_shouldReturnSNotFound_sessionNotExist() throws Exception {
        mockMvc.perform(get("/api/session/168")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest_invalidId() throws Exception {
        mockMvc.perform(get("/api/session/168L")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_shouldReturnAllSessions() throws Exception {
        mockMvc.perform(get("/api/session")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$")).isArray());
    }

    @Test
    void create_shouldReturnNewSession() throws Exception {
        SessionDto newSession = new SessionDto();
        newSession.setName("New session created");
        newSession.setDate(new Date());
        newSession.setDescription("Description of the session created");
        newSession.setTeacher_id(teacher.getId());

        mockMvc.perform(post("/api/session")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSession)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.name")).value(newSession.getName()))
                .andExpect((jsonPath("$.description")).value(newSession.getDescription()))
                .andExpect((jsonPath("$.teacher_id")).value(teacher.getId()));
    }

    @Test
    void update_shouldReturnSessionUpdated_idValid() throws Exception {
        SessionDto sessionUpdated = sessionMapper.toDto(session);
        sessionUpdated.setName("SessionUpdated");

        mockMvc.perform(put("/api/session/" + session.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionUpdated)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.name")).value(sessionUpdated.getName()));
    }

    @Test
    void update_shouldReturnBadRequest_idInvalid() throws Exception {
        SessionDto sessionUpdated = sessionMapper.toDto(session);
        sessionUpdated.setName("SessionUpdated");

        mockMvc.perform(put("/api/session/168L")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionUpdated)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_shouldReturnOkResponse_idValid() throws Exception {
        mockMvc.perform(delete("/api/session/" + session.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertNull(sessionRepository.findById(session.getId()).orElse(null));
    }

    @Test
    void delete_shouldReturnBadRequestResponse_idInvalid() throws Exception {
        mockMvc.perform(delete("/api/session/168L")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_shouldReturnNotFoundResponse_sessionNotExist() throws Exception {
        mockMvc.perform(delete("/api/session/168")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void participate_shouldReturnOkResponse() throws Exception {
        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(List.of(user), Objects.requireNonNull(sessionRepository.findById(session.getId()).orElse(null)).getUsers());
    }

    @Test
    void participate_shouldReturnBadRequestResponse_sessionInvalid() throws Exception {
        mockMvc.perform(post("/api/session/168L/participate/" + user.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void participate_shouldReturnNotFoundResponse_sessionNotExists() throws Exception {
        mockMvc.perform(post("/api/session/168/participate/" + user.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void noLongerParticipate_shouldReturnOkResponse() throws Exception {
        session.getUsers().add(user);
        sessionRepository.save(session);

        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/" + user.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(List.of(), Objects.requireNonNull(sessionRepository.findById(session.getId()).orElse(null)).getUsers());
    }

    @Test
    void noLongerParticipate_shouldReturnBadRequestResponse_userIdInvalid() throws Exception {
        session.getUsers().add(user);
        sessionRepository.save(session);
        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/168L")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noLongerParticipate_shouldReturnNotFoundResponse_sessionNotFound() throws Exception {
        session.getUsers().add(user);
        sessionRepository.save(session);
        mockMvc.perform(delete("/api/session/168/participate/" + user.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
