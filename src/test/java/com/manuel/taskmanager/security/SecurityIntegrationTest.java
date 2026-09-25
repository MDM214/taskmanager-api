package com.manuel.taskmanager.security;

import com.manuel.taskmanager.config.SecurityConfig;
import com.manuel.taskmanager.controller.TareaController;
import com.manuel.taskmanager.service.JwtService;
import com.manuel.taskmanager.service.TareaService;
import com.manuel.taskmanager.service.UsuarioService;
import com.manuel.taskmanager.controller.UsuarioController;
import com.manuel.taskmanager.controller.AuthController;
import com.manuel.taskmanager.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest({ TareaController.class, UsuarioController.class, AuthController.class })
@Import({
                SecurityConfig.class,
                JwtAuthenticationFilter.class
})
class SecurityIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private TareaService tareaService;

        @MockitoBean
        private UsuarioService usuarioService;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private UsuarioRepository usuarioRepository;

        @MockitoBean
        private PasswordEncoder passwordEncoder;

        @Test
        void tareas_sinToken_rechazaAcceso()
                        throws Exception {

                mockMvc.perform(get("/tareas"))
                                .andExpect(status().isForbidden());

                verifyNoInteractions(tareaService);
                verifyNoInteractions(usuarioService);
                verifyNoInteractions(jwtService);
        }

        @Test
        void tareas_conTokenValido_permiteAcceso()
                        throws Exception {

                String token = "token-valido";
                String username = "manuel@example.com";

                when(jwtService.validarToken(token))
                                .thenReturn(true);

                when(jwtService.extraerUsername(token))
                                .thenReturn(username);

                when(tareaService.obtenerTodas())
                                .thenReturn(List.of());

                mockMvc.perform(
                                get("/tareas")
                                                .header(
                                                                "Authorization",
                                                                "Bearer " + token))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(0));

                verify(jwtService).validarToken(token);
                verify(jwtService).extraerUsername(token);
                verify(tareaService).obtenerTodas();
                verifyNoInteractions(usuarioService);
        }

        @Test
        void tareas_conTokenInvalido_rechazaAcceso()
                        throws Exception {

                String token = "token-invalido";

                when(jwtService.validarToken(token))
                                .thenReturn(false);

                mockMvc.perform(
                                get("/tareas")
                                                .header(
                                                                "Authorization",
                                                                "Bearer " + token))
                                .andExpect(status().isForbidden());

                verify(jwtService).validarToken(token);
                verify(jwtService, never()).extraerUsername(token);
                verifyNoInteractions(tareaService);
                verifyNoInteractions(usuarioService);
        }

        @Test
        void usuarios_sinToken_rechazaAcceso()
                        throws Exception {

                mockMvc.perform(get("/usuarios"))
                                .andExpect(status().isForbidden());

                verifyNoInteractions(tareaService);
                verifyNoInteractions(usuarioService);
                verifyNoInteractions(jwtService);
        }

        @Test
        void authLogin_sinToken_permiteAcceso()
                        throws Exception {

                String json = """
                                {
                                  "email": "correo-invalido",
                                  "password": ""
                                }
                                """;

                mockMvc.perform(
                                post("/auth/login")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(json))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(usuarioRepository);
                verifyNoInteractions(passwordEncoder);
                verifyNoInteractions(jwtService);
                verifyNoInteractions(tareaService);
                verifyNoInteractions(usuarioService);
        }
}