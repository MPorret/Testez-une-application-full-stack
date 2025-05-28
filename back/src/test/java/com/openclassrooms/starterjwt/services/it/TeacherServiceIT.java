package com.openclassrooms.starterjwt.services.it;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.TeacherService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    void testFindAll_shouldReturnAllTeachers (){
        List<Teacher> allTeachers = teacherService.findAll();
        assertEquals(1, allTeachers.size());
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

    @SpringBootTest
    @TestPropertySource(properties = {
            "oc.app.jwtSecret=testSecretKey123456",
            "oc.app.jwtExpirationMs=3600000"
    })
    public static class JwtUtilsIT {
        @Autowired
        private JwtUtils jwtUtils;

        @Test
        void validateJwtToken_shouldReturnFalse_whenMalformedToken() {
            String malformedToken = "malformated.token";
            assertFalse(jwtUtils.validateJwtToken(malformedToken));
        }

        @Test
        void validateJwtToken_shouldReturnFalse_whenNull() {
            assertFalse(jwtUtils.validateJwtToken(null));
        }

        @Test
        void validateJwtToken_shouldReturnFalse_whenEmpty() {
            assertFalse(jwtUtils.validateJwtToken(""));
        }

        @Test
        void validateJwtToken_shouldReturnFalse_whenExpired() {
            String expiredToken = Jwts.builder()
                    .setSubject("testuser@example.com")
                    .setIssuedAt(new Date(System.currentTimeMillis() - 7200000))  // issued 2h ago
                    .setExpiration(new Date(System.currentTimeMillis() - 1000))   // expired 1s ago
                    .signWith(SignatureAlgorithm.HS512, "testSecretKey123456")
                    .compact();

            assertFalse(jwtUtils.validateJwtToken(expiredToken));
        }
    }
}
