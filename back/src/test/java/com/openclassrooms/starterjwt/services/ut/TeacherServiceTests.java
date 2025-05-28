package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
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
public class TeacherServiceTests {
    private static final Long teacherId = 3L;

    @Mock
    TeacherRepository teacherRepository;
    @InjectMocks
    TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void init() {
        teacher = new Teacher(
                teacherId,
                "Teacher",
                "Test",
                LocalDateTime.now(),
                LocalDateTime.now());
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
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.ofNullable(teacher));
        Teacher result = teacherService.findById(teacherId);
        assertEquals(result, teacher);
        verify(teacherRepository).findById(teacherId);
    }

    @Test
    void findById_shouldThrowError_TeacherNotExists() {
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());
        Teacher result = teacherService.findById(teacherId);
        assertNull(result);
        verify(teacherRepository).findById(teacherId);
    }
}
