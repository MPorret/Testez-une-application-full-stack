package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private final TeacherMapper teacherMapper;
    private final TeacherService teacherService;


    public TeacherController(TeacherService teacherService,
                             TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
        this.teacherService = teacherService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        try {
            Teacher teacher = this.teacherService.findById(Long.valueOf(id));

            if (teacher == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok().body(this.teacherMapper.toDto(teacher));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    public ResponseEntity<?> findAll() {
        List<Teacher> teachers = this.teacherService.findAll();

        return ResponseEntity.ok().body(this.teacherMapper.toDto(teachers));
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody TeacherDto teacherDto) {

        Teacher teacher = this.teacherService.create(this.teacherMapper.toEntity(teacherDto));

        return ResponseEntity.ok().body(this.teacherMapper.toDto(teacher));
    }

}
