package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;
    @InjectMocks
    private TeacherService teacherService;
    private Teacher teacher;


    @BeforeEach
    void setUp() {
        teacher = Teacher.builder().id(1L)
                .firstName("Jhon")
                .lastName("Mohamed")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));
        List<Teacher> teachers = teacherService.findAll();
        Assertions.assertNotNull(teachers);
        Assertions.assertEquals(1, teachers.size());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        Teacher teacher = teacherService.findById(1L);
        Assertions.assertNotNull(teacher);
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void create() {
        when(teacherRepository.save(teacher)).thenReturn(teacher);
        Teacher teacher1 = teacherService.create(teacher);
        Assertions.assertNotNull(teacher1);
        verify(teacherRepository, times(1)).save(teacher);

    }
}