package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TeacherMapperImplTest {

    @InjectMocks
    private TeacherMapperImpl teacherMapper;
    @Mock
    private TeacherDto teacherDto;
    @Mock
    private Teacher teacher;



    @Mock
    private LocalDateTime createdAt;
    @Mock
    private LocalDateTime updatedAt;

    @BeforeEach
    void setUp() {
        teacher.setId(1L);
    }

    @Test
    void toEntity_withTeacherDtoIsNull_shouldReturnNull() {
        teacherDto = null;
        assertNull(teacherMapper.toEntity(teacherDto));
    }
    @Test
    void toEntity_withTeacherDtoIsNotNull_shouldReturnTeacherEntity() {

        Teacher teacherEntity = teacherMapper.toEntity(teacherDto);
        Teacher expectedTeacherEntity = new Teacher(teacherDto.getId(), teacherDto.getLastName(),
                teacherDto.getFirstName(), teacherDto.getCreatedAt(),teacherDto.getUpdatedAt());
        assertEquals(expectedTeacherEntity, teacherEntity);
    }

    @Test
    void toDto_withTeacherIsNull_shouldReturnNull() {
        teacher = null;
        assertNull(teacherMapper.toDto(teacher));
    }

    @Test
    void toDto_withTeacherIsNotNull_shouldReturnTeacherDto() {
        TeacherDto expectedTeacherDto = new TeacherDto(teacher.getId(),teacher.getLastName(),
                teacher.getFirstName(),teacher.getCreatedAt(),teacher.getUpdatedAt());
        TeacherDto actualTeacherDto = teacherMapper.toDto(teacher);
        assertEquals(expectedTeacherDto, actualTeacherDto);
        assertNotNull(teacher);
    }

    @Test
    void testToEntity_withTeacherDtoListIsNull_shouldReturnNull() {
        List<TeacherDto> teacherDtoList = null;
        assertNull(teacherMapper.toEntity(teacherDtoList));
    }
    @Test
    void testToEntity_withTeacherDtoListIsNotNull_shouldReturnEntityList() {
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teacherDtoList.add(teacherDto);
        List<Teacher> teachers = teacherMapper.toEntity(teacherDtoList);
        assertNotNull(teachers);
        assertEquals(teacherDtoList.size(), teachers.size());
        IntStream.range(0, teachers.size()).forEach(i ->
                assertEquals(teacherDtoList.get(i).getId(), teachers.get(i).getId())
        );
    }

    @Test
    void testToDto_withTeacherListIsNull_shouldReturnNull() {
        List<Teacher> teacherList = null;
        assertNull(teacherMapper.toDto(teacherList));
    }
    @Test
    void testToDto_withTeacherListIsNotNull_shouldReturnDtoList() {
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);
        assertNotNull(teacherDtoList);
        assertEquals(teacherDtoList.size(), teacherList.size());
        IntStream.range(0, teacherDtoList.size()).forEach(i ->
                assertEquals(teacherDtoList.get(i).getId(), teacherList.get(i).getId())
        );
    }
}