package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;
    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp(){
        teacher = new Teacher(1L,"John","Mbarek", LocalDateTime.now(),
                LocalDateTime.now());
        teacherDto = new TeacherDto(1L,"John","Mbarek", LocalDateTime.now(),
                LocalDateTime.now());

    }

    @Test
    void findById_shouldReturnNotFoundWhenTeacherDoesNotExistById() {
        when(teacherService.findById(0L)).thenReturn(null);
        ResponseEntity<?> response = teacherController.findById("0");
        assertEquals(ResponseEntity.notFound().build(), response);

    }
    @Test
    void findById_shouldReturnBadRequestWhenIdIsInvalid() {
        ResponseEntity<?> response = teacherController.findById("0jjjj");
        assertEquals(ResponseEntity.badRequest().build(), response);

    }
    @Test
    void findById_shouldReturnTeacherWhenTeacherExists() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);
        ResponseEntity<?> response = teacherController.findById("1");
        assertEquals(teacherDto, response.getBody());

    }

    @Test
    void findAll() {
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Gare aux questions");
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        teachers.add(teacher2);
        List<TeacherDto> teachersDto = new ArrayList<>();
        teachersDto.add(teacherDto);
        teachersDto.add(new TeacherDto());
        when(teacherMapper.toDto(teachers)).thenReturn(teachersDto);

        when(teacherService.findAll()).thenReturn(List.of(teacher,teacher2));
        ResponseEntity<?> response = teacherController.findAll();
        assertEquals(teachersDto, response.getBody());
        assertEquals(2,teachersDto.size());
    }

    @Test
    void create() {
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);
        when (teacherMapper.toEntity(teacherDto)).thenReturn(teacher);
        when(teacherService.create(teacher)).thenReturn(teacher);
        ResponseEntity<?> response = teacherController.create(teacherDto);
        assertEquals(teacherDto, response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
}