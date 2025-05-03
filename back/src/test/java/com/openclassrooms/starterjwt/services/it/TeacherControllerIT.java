package com.openclassrooms.starterjwt.services.it;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class TeacherControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeEach
    void init() {
        Teacher newTeacher = new Teacher();
        newTeacher.setLastName("Test")
                .setFirstName("Teacher");
        teacher = teacherRepository.save(newTeacher);
    }

    @Test
    void findById_shouldReturnTeacher_validId() throws Exception {
        mockMvc.perform(get("/api/teacher/" + teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect((jsonPath("$.id")).value(teacher.getId()));
    }

    @Test
    void findById_shouldReturnNull_teacherNotExists() throws Exception {
        mockMvc.perform(get("/api/teacher/467"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest_idInvalid() throws Exception {
        mockMvc.perform(get("/api/teacher/invalidid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_shouldReturnOkResponse() throws Exception {
        mockMvc.perform(get("/api/teacher/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect((jsonPath("$")).isArray());
    }
}
