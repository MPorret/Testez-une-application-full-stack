package com.openclassrooms.starterjwt.security.jwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthEntryPointJwtTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void commence_ShouldReturnUnauthorizedResponse() throws Exception {
        // Mock de authException et request
        lenient().when(authException.getMessage()).thenReturn("Unauthorized access");
        when(request.getServletPath()).thenReturn("/api/test");

        // Configuration du flux de sortie de la réponse
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new ServletOutputStreamMock(outputStream));

        // Appel de la méthode commence
        authEntryPointJwt.commence(request, response, authException);

        // Vérification du statut et du type de contenu
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Conversion de la sortie en Map pour comparaison
        Map<String, Object> responseBody = mapper.readValue(outputStream.toByteArray(), Map.class);

        // Assertions pour valider le résultat attendu
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseBody.get("status"));
        assertEquals("Unauthorized", responseBody.get("error"));
        assertEquals("Unauthorized access", responseBody.get("message"));
        assertEquals("/api/test", responseBody.get("path"));
    }

    // Classe interne pour simuler un ServletOutputStream
    static class ServletOutputStreamMock extends javax.servlet.ServletOutputStream {
        private final ByteArrayOutputStream outputStream;

        ServletOutputStreamMock(ByteArrayOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) {
            outputStream.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(javax.servlet.WriteListener writeListener) {
            // Méthode vide car non nécessaire pour le test
        }
    }
}
