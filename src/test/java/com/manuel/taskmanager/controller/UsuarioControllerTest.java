package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.exception.RecursoNoEncontradoException;
import com.manuel.taskmanager.exception.GlobalExceptionHandler;
import com.manuel.taskmanager.service.UsuarioService;

import org.hibernate.annotations.Changelog.Timestamp;
import org.hibernate.validator.constraints.Mod10Check;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).setControllerAdvice(new GlobalExceptionHandler())
                .build();

    }

    @Test
    void obtenerTodos_devuelveListaYEstado200()
            throws Exception {

        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Manuel");
        usuario1.setEmail("manuel@example.com");

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Laura");
        usuario2.setEmail("laura@example.com");

        when(usuarioService.obtenerTodos()).thenReturn(List.of(usuario1, usuario2));

        mockMvc.perform(get("/usuarios")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Manuel"))
                .andExpect(jsonPath("$[0].email").value("manuel@example.com"))
                .andExpect(jsonPath("$[1].nombre").value("Laura"))
                .andExpect(jsonPath("$[1].email").value("laura@example.com"))
                .andExpect(jsonPath("$[0].password").doesNotExist()).andExpect(jsonPath("$[0].rol").doesNotExist());

        verify(usuarioService).obtenerTodos();
    }

    @Test
    void obtenerPorId_cuandoExiste_devuelveUsuarioYEstado200()
            throws Exception {

        Long id = 1L;

        Usuario usuario = new Usuario();
        usuario.setNombre("Manuel");
        usuario.setEmail("manuel@example.com");
        usuario.setPassword("passwordCodificada");
        usuario.setRol("USER");

        when(usuarioService.obtenerPorId(id))
                .thenReturn(usuario);

        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.nombre")
                                .value("Manuel"))
                .andExpect(
                        jsonPath("$.email")
                                .value("manuel@example.com"))
                .andExpect(
                        jsonPath("$.password")
                                .doesNotExist())
                .andExpect(
                        jsonPath("$.rol")
                                .doesNotExist());

        verify(usuarioService).obtenerPorId(id);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_devuelveEstado404()
            throws Exception {

        Long id = 99L;

        when(usuarioService.obtenerPorId(id))
                .thenThrow(
                        new RecursoNoEncontradoException(
                                "Usuario no encontrado con ID: " + id));

        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.mensaje")
                                .value(
                                        "Usuario no encontrado con ID: " + id));

        verify(usuarioService).obtenerPorId(id);
    }

    @Test
    void crearUsuario_conDatosValidos_devuelveUsuarioYEstado200()
            throws Exception {

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setNombre("Manuel");
        usuarioGuardado.setEmail("manuel@example.com");
        usuarioGuardado.setPassword("passwordCodificada");
        usuarioGuardado.setRol("USER");

        when(usuarioService.guardar(any(Usuario.class)))
                .thenReturn(usuarioGuardado);

        String json = """
                {
                  "nombre": "Manuel",
                  "email": "manuel@example.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(
                post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.nombre")
                                .value("Manuel"))
                .andExpect(
                        jsonPath("$.email")
                                .value("manuel@example.com"))
                .andExpect(
                        jsonPath("$.password")
                                .doesNotExist())
                .andExpect(
                        jsonPath("$.rol")
                                .doesNotExist());

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);

        verify(usuarioService).guardar(captor.capture());

        Usuario usuarioRecibido = captor.getValue();

        assertEquals(
                "Manuel",
                usuarioRecibido.getNombre());

        assertEquals(
                "manuel@example.com",
                usuarioRecibido.getEmail());

        assertEquals(
                "password123",
                usuarioRecibido.getPassword());
    }

    @Test
    void crearUsuario_conDatosInvalidos_devuelveEstado400()
            throws Exception {

        String json = """
                {
                  "nombre": "",
                  "email": "correo-invalido",
                  "password": "123"
                }
                """;

        mockMvc.perform(
                post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.mensaje").exists());

        verifyNoInteractions(usuarioService);
    }

    @Test
    void actualizarUsuario_conDatosValidos_devuelveUsuarioYEstado200()
            throws Exception {

        Long id = 1L;

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Manuel actualizado");
        usuarioActualizado.setEmail("actualizado@example.com");
        usuarioActualizado.setPassword("passwordCodificada");
        usuarioActualizado.setRol("USER");

        when(usuarioService.actualizar(
                eq(id),
                any(Usuario.class))).thenReturn(usuarioActualizado);

        String json = """
                {
                  "nombre": "Manuel actualizado",
                  "email": "actualizado@example.com"
                }
                """;

        mockMvc.perform(
                put("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.nombre")
                                .value("Manuel actualizado"))
                .andExpect(
                        jsonPath("$.email")
                                .value("actualizado@example.com"))
                .andExpect(
                        jsonPath("$.password")
                                .doesNotExist())
                .andExpect(
                        jsonPath("$.rol")
                                .doesNotExist());

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);

        verify(usuarioService).actualizar(
                eq(id),
                captor.capture());

        Usuario usuarioRecibido = captor.getValue();

        assertEquals(
                "Manuel actualizado",
                usuarioRecibido.getNombre());

        assertEquals(
                "actualizado@example.com",
                usuarioRecibido.getEmail());
    }

    @Test
    void actualizarUsuario_conDatosInvalidos_devuelveEstado400()
            throws Exception {

        Long id = 1L;

        String json = """
                {
                  "nombre": "",
                  "email": "correo-invalido"
                }
                """;

        mockMvc.perform(
                put("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.mensaje").exists());

        verifyNoInteractions(usuarioService);
    }

    @Test
    void eliminarUsuario_cuandoExiste_devuelveEstado204()
            throws Exception {

        Long id = 1L;

        mockMvc.perform(delete("/usuarios/{id}", id))
                .andExpect(status().isNoContent());

        verify(usuarioService).eliminar(id);
    }

    @Test
    void eliminarUsuario_cuandoNoExiste_devuelveEstado404()
            throws Exception {

        Long id = 99L;

        org.mockito.Mockito.doThrow(
                new RecursoNoEncontradoException(
                        "Usuario no encontrado con ID: " + id))
                .when(usuarioService).eliminar(id);

        mockMvc.perform(delete("/usuarios/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.mensaje")
                                .value(
                                        "Usuario no encontrado con ID: " + id));

        verify(usuarioService).eliminar(id);
    }

}