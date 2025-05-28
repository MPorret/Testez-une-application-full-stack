package com.openclassrooms.starterjwt.services.ut;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class TeacherMapperTests {
    @Autowired
    private TeacherMapper teacherMapper;

    private TeacherDto teacherDto;
    private Teacher teacher;

    @BeforeEach
    void init () {
        teacherDto = new TeacherDto(
                2L,
                "John",
                "Doe",
                LocalDateTime.now(),
                LocalDateTime.now());

        teacher = new Teacher()
                .setId(2L)
                .setLastName("John")
                .setFirstName("Doe")
                .setCreatedAt(teacherDto.getCreatedAt())
                .setUpdatedAt(teacherDto.getUpdatedAt());
    }

    @Test
    void ToEntity_shouldBeEquals(){
        assertEquals(teacherMapper.toEntity(teacherDto), teacher);
    }

    @Test
    void ToEntity_shouldReturnNull(){
        TeacherDto teacher = null;
        assertNull(teacherMapper.toEntity(teacher));
    }

    @Test
    void ToDto_shouldReturnNull(){
        Teacher teacher = null;
        assertNull(teacherMapper.toDto(teacher));
    }

    @Test
    void listToEntity_shouldBeEquals(){
        assertEquals(teacherMapper.toEntity(List.of(teacherDto)), List.of(teacher));
    }

    @Test
    void listToDto_shouldBeEquals(){
        assertEquals(teacherMapper.toDto(List.of(teacher)), List.of(teacherDto));
    }


    @Test
    void listToEntity_shouldReturnNull(){
        List<TeacherDto> teacherList = null;
        assertNull(teacherMapper.toEntity(teacherList));
    }

    @Test
    void listToDto_shouldReturnNull(){
        List<Teacher> teacherList = null;
        assertNull(teacherMapper.toDto(teacherList));
    }
}
