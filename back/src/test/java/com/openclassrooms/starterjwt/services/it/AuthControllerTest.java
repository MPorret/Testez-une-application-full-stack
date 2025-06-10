package com.openclassrooms.starterjwt.services.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init(){
        User user = new User(
                "newuser@example.com",
                "John",
                "Doe",
                passwordEncoder.encode("test123"),
                false);

        userRepository.save(user);
    }

    @Test
    void login_shouldReturnUserDetails() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("newuser@example.com");
        request.setPassword("test123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.token")).exists())
                .andExpect((jsonPath("$.username")).value("newuser@example.com"))
                .andExpect((jsonPath("$.admin")).value(false));
    }

    @Test
    void register_shouldReturnOk_userValid() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setEmail("register-newuser@example.com");
        request.setPassword("test123");
        request.setFirstName("User");
        request.setLastName("Test");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.message")).value("User registered successfully!"));
    }

    @Test
    void register_shouldReturnOk_userInvalid() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setEmail("newuser@example.com");
        request.setPassword("test123");
        request.setFirstName("User");
        request.setLastName("Test");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$.message")).value("Error: Email is already taken!"));
    }
}
