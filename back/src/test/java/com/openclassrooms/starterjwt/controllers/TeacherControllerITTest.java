package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeacherControllerITTest {

    public static final String FIRST_NAME = "Haraprasad";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TeacherRepository teacherRepository;
    private Teacher teacher;
    private LocalDateTime NOW = LocalDateTime.now();
    static Logger logger = LoggerFactory.getLogger(TeacherControllerITTest.class);
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        logger.info("TeacherControllerITTest setUpBeforeAll");

    }


    @BeforeEach
    public void setUp() throws Exception {
        logger.info("TeacherControllerITTest setUpBeforeEach");
        teacher = Teacher.builder().id(1L)
                .firstName(FIRST_NAME)
                .lastName("Victor")
                .createdAt(NOW)
                .updatedAt(NOW).build();
        teacherRepository.save(teacher);

    }

    @Test
    @WithMockUser
    void test_getAllTeachers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/teacher"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", is(FIRST_NAME)));

    }
    @Test
    @WithMockUser
    void test_getTeacherById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/teacher/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is(FIRST_NAME)));

    }

    @Test
    @WithMockUser
    public void test_create_returnOk() {
        try {
            List<Teacher> teachers = teacherRepository.findAll();
            logger.info("sessions: " + teachers);
            int previousTeachersCount = teachers.size();
String jsonTeacher = "{\n" +
            "  \n" +
                    "        \"lastName\": \"ABDEL\",\n" +
                    "        \"firstName\": \"Karim\",\n" +
                    "        \"createdAt\": \"2024-10-15T05:27:53\",\n" +
                    "        \"updatedAt\": \"2024-10-15T05:27:53\"\n" +
                    "    }";

            mockMvc.perform(MockMvcRequestBuilders.post("/api/teacher")
                            .contentType("application/json")
                            .content(jsonTeacher))

                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("Karim")));
            teachers = teacherRepository.findAll();
            int actualTeachersCount = teachers.size();
            Assertions.assertEquals(previousTeachersCount+1, actualTeachersCount);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Invalid Teacher Data", e);
        }
    }
}
