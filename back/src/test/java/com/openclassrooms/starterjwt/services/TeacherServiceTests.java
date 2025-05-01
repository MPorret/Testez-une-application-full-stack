package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTests {
    @Mock
    TeacherRepository teacherRepository;
    @InjectMocks
    TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void init() {
        teacher = new Teacher();
        teacher.setId(3L);
    }

    @Test
    void findAll_shouldCallFindAll() {
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));
        List<Teacher> result = teacherService.findAll();
        assertEquals(result, List.of(teacher));
        verify(teacherRepository).findAll();
    }

    @Test
    void findById_shouldCallFindById() {
        when(teacherRepository.findById(3L)).thenReturn(Optional.ofNullable(teacher));
        Teacher result = teacherService.findById(3L);
        assertEquals(result, teacher);
        verify(teacherRepository).findById(3L);
    }

    @Test
    void findById_shouldThrowError_TeacherNotExists() {
        when(teacherRepository.findById(3L)).thenReturn(Optional.empty());
        Teacher result = teacherService.findById(3L);
        assertNull(result);
        verify(teacherRepository).findById(3L);
    }
}
