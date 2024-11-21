package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @Mock
    private Authentication authentication;
    @Mock
    private UserDetails userDetails;

    private User user1;


    @BeforeEach
    public void setUp() {
       user1  = new User("test@gmail.com","Hosni","Brahim",
                "pass@word",false);
         userDetails = new UserDetailsImpl(1L, "test@gmail.com", "firstName", "lastName", false, "pass@word");


    }




    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        // Création de la requête d'inscription avec un email existant
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("existinguser@test.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("FirstName");
        signupRequest.setLastName("LastName");

        // Simulation de la présence de l'email dans le repository
        when(userRepository.existsByEmail("existinguser@test.com")).thenReturn(true);

        // Appel de la méthode registerUser
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        // Vérification de la réponse pour un email existant
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());
    }

    @Test
    public void testRegisterUser_Success() {
        // Création de la requête d'inscription pour un nouvel utilisateur
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@test.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("FirstName");
        signupRequest.setLastName("LastName");

        // Simulation d'un email inexistant
        when(userRepository.existsByEmail("newuser@test.com")).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        // Appel de la méthode registerUser
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        // Vérification que l'utilisateur a été sauvegardé
        verify(userRepository).save(any(User.class));

        // Vérification de la réponse de succès
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());
    }

    @Test
    public void testAuthenticateUser() {
        // Préparation des mocks
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("pass@word");

        // Simulation de l'authentification
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Configuration du token JWT et du SecurityContext
        String jwt = "HSDFSFSF555AA:156RSA";
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwt);

        // Mocking du SecurityContext
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Simulation de l'utilisateur dans UserDetails0
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user1));

        // Appel de la méthode testée
        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        // Vérifications
        assertNotNull(user1);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertEquals(jwt, jwtResponse.getToken());
    }

}
