package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.entity.Tarea;
import com.manuel.taskmanager.exception.RecursoNoEncontradoException;
import com.manuel.taskmanager.exception.GlobalExceptionHandler;
import com.manuel.taskmanager.service.TareaService;
import com.manuel.taskmanager.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.manuel.taskmanager.entity.Usuario;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@ExtendWith(MockitoExtension.class)
class TareaControllerTest {

    @Mock
    private TareaService tareaService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private TareaController tareaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(tareaController)
                .setControllerAdvice(
                        new GlobalExceptionHandler()
                )
                .build();
    }

    @Test
    void obtenerTodas_devuelveListaYEstado200()
            throws Exception {

        Tarea tarea1 = new Tarea();
        tarea1.setTitulo("Preparar documentación");
        tarea1.setEstado("PENDIENTE");

        Tarea tarea2 = new Tarea();
        tarea2.setTitulo("Crear pruebas");
        tarea2.setEstado("COMPLETADA");

        when(tareaService.obtenerTodas())
                .thenReturn(List.of(tarea1, tarea2));

        mockMvc.perform(get("/tareas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(
                        jsonPath("$[0].titulo")
                                .value("Preparar documentación")
                )
                .andExpect(
                        jsonPath("$[0].estado")
                                .value("PENDIENTE")
                )
                .andExpect(
                        jsonPath("$[1].titulo")
                                .value("Crear pruebas")
                )
                .andExpect(
                        jsonPath("$[1].estado")
                                .value("COMPLETADA")
                );

        verify(tareaService).obtenerTodas();
    }

    @Test
void obtenerPorId_cuandoExiste_devuelveTareaYEstado200()
        throws Exception {

    Long id = 1L;

    Tarea tarea = new Tarea();
    tarea.setTitulo("Preparar documentación");
    tarea.setEstado("PENDIENTE");

    when(tareaService.obtenerPorId(id))
            .thenReturn(tarea);

    mockMvc.perform(get("/tareas/{id}", id))
            .andExpect(status().isOk())
            .andExpect(
                    jsonPath("$.titulo")
                            .value("Preparar documentación")
            )
            .andExpect(
                    jsonPath("$.estado")
                            .value("PENDIENTE")
            );

    verify(tareaService).obtenerPorId(id);
    }

    @Test
void obtenerPorId_cuandoNoExiste_devuelveEstado404()
        throws Exception {

    Long id = 99L;

    when(tareaService.obtenerPorId(id))
            .thenThrow(
                    new RecursoNoEncontradoException(
                            "Tarea no encontrada con ID: " + id
                    )
            );

    mockMvc.perform(get("/tareas/{id}", id))
            .andExpect(status().isNotFound())
            .andExpect(
                    jsonPath("$.mensaje")
                            .value(
                                    "Tarea no encontrada con ID: " + id
                            )
            );

    verify(tareaService).obtenerPorId(id);
    }

    @Test
void crearTarea_conDatosValidos_devuelveTareaYEstado200()
        throws Exception {

    Long usuarioId = 1L;
    Usuario usuario = new Usuario();

    Tarea tareaGuardada = new Tarea();
    tareaGuardada.setTitulo("Preparar documentación");
    tareaGuardada.setDescripcion("Completar la guía");
    tareaGuardada.setEstado("PENDIENTE");
    tareaGuardada.setUsuario(usuario);

    when(usuarioService.obtenerPorId(usuarioId))
            .thenReturn(usuario);

    when(tareaService.guardar(any(Tarea.class)))
            .thenReturn(tareaGuardada);

    String json = """
            {
              "titulo": "Preparar documentación",
              "descripcion": "Completar la guía",
              "estado": "PENDIENTE",
              "usuarioId": 1
            }
            """;

    mockMvc.perform(
                    post("/tareas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            )
            .andExpect(status().isOk())
            .andExpect(
                    jsonPath("$.titulo")
                            .value("Preparar documentación")
            )
            .andExpect(
                    jsonPath("$.estado")
                            .value("PENDIENTE")
            );

    verify(usuarioService).obtenerPorId(usuarioId);

    ArgumentCaptor<Tarea> captor =
            ArgumentCaptor.forClass(Tarea.class);

    verify(tareaService).guardar(captor.capture());

    Tarea tareaRecibida = captor.getValue();

    assertEquals(
            "Preparar documentación",
            tareaRecibida.getTitulo()
    );

    assertEquals(
            "Completar la guía",
            tareaRecibida.getDescripcion()
    );

    assertEquals(
            "PENDIENTE",
            tareaRecibida.getEstado()
    );

    assertSame(
            usuario,
            tareaRecibida.getUsuario()
    );
    }

    @Test
void crearTarea_conDatosInvalidos_devuelveEstado400()
        throws Exception {

    String json = """
            {
              "titulo": "",
              "descripcion": "Descripción válida",
              "estado": ""
            }
            """;

    mockMvc.perform(
                    post("/tareas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                    jsonPath("$.mensaje").exists()
            );

    verifyNoInteractions(usuarioService);
    verifyNoInteractions(tareaService);
    }

    @Test
void actualizar_conDatosValidos_devuelveTareaYEstado200()
        throws Exception {

    Long id = 1L;

    Tarea tareaActualizada = new Tarea();
    tareaActualizada.setTitulo("Título actualizado");
    tareaActualizada.setDescripcion("Descripción actualizada");
    tareaActualizada.setEstado("COMPLETADA");

    when(tareaService.actualizar(
            org.mockito.ArgumentMatchers.eq(id),
            any(Tarea.class)
    )).thenReturn(tareaActualizada);

    String json = """
            {
              "titulo": "Título actualizado",
              "descripcion": "Descripción actualizada",
              "estado": "COMPLETADA"
            }
            """;

    mockMvc.perform(
                    put("/tareas/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            )
            .andExpect(status().isOk())
            .andExpect(
                    jsonPath("$.titulo")
                            .value("Título actualizado")
            )
            .andExpect(
                    jsonPath("$.estado")
                            .value("COMPLETADA")
            );

    ArgumentCaptor<Tarea> captor =
            ArgumentCaptor.forClass(Tarea.class);

    verify(tareaService).actualizar(
            org.mockito.ArgumentMatchers.eq(id),
            captor.capture()
    );

    Tarea tareaRecibida = captor.getValue();

    assertEquals(
            "Título actualizado",
            tareaRecibida.getTitulo()
    );

    assertEquals(
            "Descripción actualizada",
            tareaRecibida.getDescripcion()
    );

    assertEquals(
            "COMPLETADA",
            tareaRecibida.getEstado()
    );
    }

    @Test
void actualizar_conDatosInvalidos_devuelveEstado400()
        throws Exception {

    Long id = 1L;

    String json = """
            {
              "titulo": "",
              "descripcion": "Descripción actualizada",
              "estado": "COMPLETADA"
            }
            """;

    mockMvc.perform(
                    put("/tareas/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                    jsonPath("$.mensaje").exists()
            );

    verifyNoInteractions(tareaService);
    verifyNoInteractions(usuarioService);
    }

    @Test
void eliminar_cuandoExiste_devuelveEstado204()
        throws Exception {

    Long id = 1L;

    mockMvc.perform(delete("/tareas/{id}", id))
            .andExpect(status().isNoContent());

    verify(tareaService).eliminar(id);
    verifyNoInteractions(usuarioService);
    }

    @Test
void eliminar_cuandoNoExiste_devuelveEstado404()
        throws Exception {

    Long id = 99L;

    org.mockito.Mockito.doThrow(
            new RecursoNoEncontradoException(
                    "Tarea no encontrada con ID: " + id
            )
    ).when(tareaService).eliminar(id);

    mockMvc.perform(delete("/tareas/{id}", id))
            .andExpect(status().isNotFound())
            .andExpect(
                    jsonPath("$.mensaje")
                            .value(
                                    "Tarea no encontrada con ID: " + id
                            )
            );

    verify(tareaService).eliminar(id);
    verifyNoInteractions(usuarioService);
    }
}