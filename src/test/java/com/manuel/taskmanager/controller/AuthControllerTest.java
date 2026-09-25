package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.exception.GlobalExceptionHandler;
import com.manuel.taskmanager.repository.UsuarioRepository;
import com.manuel.taskmanager.service.JwtService;
import com.manuel.taskmanager.exception.CredencialesInvalidasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(
                        new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() {

        SecurityContextHolder.clearContext();
    }

    @Test
    void login_conCredencialesValidas_devuelveTokenYEstado200()
            throws Exception {

        String email = "manuel@example.com";
        String passwordOriginal = "password123";
        String passwordCodificada = "passwordCodificada";
        String token = "token-jwt-de-prueba";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(passwordCodificada);
        usuario.setRol("USER");

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(
                passwordOriginal,
                passwordCodificada)).thenReturn(true);

        when(jwtService.generarToken(email))
                .thenReturn(token);

        String json = """
                {
                  "email": "manuel@example.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(
                post("/auth/login")
                        .contentType(
                                MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.token")
                                .value(token));

        verify(usuarioRepository).findByEmail(email);

        verify(passwordEncoder).matches(
                passwordOriginal,
                passwordCodificada);

        verify(jwtService).generarToken(email);
    }

    @Test
    void login_conUsuarioInexistente_devuelveEstado401()
            throws Exception {

        String email = "inexistente@example.com";

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        String json = """
                {
                  "email": "inexistente@example.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(
                post("/auth/login")
                        .contentType(
                                MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(
                        jsonPath("$.mensaje").exists());

        verify(usuarioRepository).findByEmail(email);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
    }

    @Test
    void login_conPasswordIncorrecta_devuelveEstado401()
            throws Exception {

        String email = "manuel@example.com";
        String passwordRecibida = "passwordIncorrecta";
        String passwordCodificada = "passwordCodificada";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(passwordCodificada);
        usuario.setRol("USER");

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(
                passwordRecibida,
                passwordCodificada)).thenReturn(false);

        String json = """
                {
                  "email": "manuel@example.com",
                  "password": "passwordIncorrecta"
                }
                """;

        mockMvc.perform(
                post("/auth/login")
                        .contentType(
                                MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(
                        jsonPath("$.mensaje").exists());

        verify(usuarioRepository).findByEmail(email);

        verify(passwordEncoder).matches(
                passwordRecibida,
                passwordCodificada);

        verifyNoInteractions(jwtService);
    }

    @Test
    void login_conDatosInvalidos_devuelveEstado400()
            throws Exception {

        String json = """
                {
                  "email": "correo-invalido",
                  "password": ""
                }
                """;

        mockMvc.perform(
                post("/auth/login")
                        .contentType(
                                MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.mensaje").exists());

        verifyNoInteractions(usuarioRepository);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
    }

    @Test
    void whoami_conAutenticacion_devuelveUsername()
            throws Exception {

        String email = "manuel@example.com";

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                email,
                null,
                java.util.List.of());

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        mockMvc.perform(get("/auth/whoami"))
                .andExpect(status().isOk())
                .andExpect(
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
                                .string(email));

        verifyNoInteractions(usuarioRepository);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
    }
}
