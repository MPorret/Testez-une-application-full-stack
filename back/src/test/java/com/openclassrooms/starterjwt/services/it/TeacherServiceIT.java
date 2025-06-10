package com.openclassrooms.starterjwt.services.it;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TeacherServiceIT {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void init() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("User");
        this.teacher = teacherRepository.save(teacher);
    }

    @Test
    void testFindById_shouldReturnTeacher() {
        Teacher teacherFound = teacherService.findById(teacher.getId());
        assertEquals(teacherFound.getId(), teacher.getId());
    }

    @Test
    void testFindById_shouldReturnNull() {
        Teacher teacherFound = teacherService.findById(164L);
        assertNull(teacherFound);
    }
}
