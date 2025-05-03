package com.openclassrooms.starterjwt.services.it;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void init() {
        User newUser = new User();
        newUser.setEmail("test@user.com")
                .setLastName("Test")
                .setFirstName("User");
        user = userRepository.save(newUser);
    }

    @Test
    void findById_shouldReturnUserDto_validId() throws Exception {
        mockMvc.perform(get("/api/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect((jsonPath("$.id")).value(user.getId()));
    }

    @Test
    void findById_shouldReturnNull_userNotExists() throws Exception {
        mockMvc.perform(get("/api/user/467"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest_idInvalid() throws Exception {
        mockMvc.perform(get("/api/user/invalidid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@user.com", roles = {"USER"})
    void delete_shouldReturnOkResponse_validId() throws Exception {
        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturnNotFound_userNotExists() throws Exception {
        mockMvc.perform(delete("/api/user/467"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnBadRequest_invalidId() throws Exception {
        mockMvc.perform(delete("/api/user/invalidid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "first@user.com", roles = {"USER"})
    void delete_shouldReturnUnauthorized_notAuthUser() throws Exception {
        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isUnauthorized());
    }
}
