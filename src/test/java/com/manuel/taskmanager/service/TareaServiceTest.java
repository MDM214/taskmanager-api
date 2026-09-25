package com.manuel.taskmanager.service;

import com.manuel.taskmanager.entity.Tarea;
import com.manuel.taskmanager.exception.RecursoNoEncontradoException;
import com.manuel.taskmanager.repository.TareaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private TareaService tareaService;

    @Test
    void obtenerTodas_devuelveListaDeTareas() {

        Tarea tarea1 = new Tarea();
        Tarea tarea2 = new Tarea();

        List<Tarea> tareasEsperadas = List.of(tarea1, tarea2);

        when(tareaRepository.findAll()).thenReturn(tareasEsperadas);

        List<Tarea> resultado = tareaService.obtenerTodas();

        assertEquals(tareasEsperadas, resultado);
        verify(tareaRepository).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_devuelveTarea() {

        Long id = 1L;
        Tarea tareaEsperada = new Tarea();

        when(tareaRepository.findById(id))
                .thenReturn(Optional.of(tareaEsperada));

        Tarea resultado = tareaService.obtenerPorId(id);

        assertEquals(tareaEsperada, resultado);
        verify(tareaRepository).findById(id);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_lanzaExcepcion() {

        Long id = 99L;

        when(tareaRepository.findById(id))
                .thenReturn(Optional.empty());

        RecursoNoEncontradoException excepcion = assertThrows(
                RecursoNoEncontradoException.class,
                () -> tareaService.obtenerPorId(id));

        assertEquals("Tarea no encontrada con ID: " + id,
                excepcion.getMessage());

        verify(tareaRepository).findById(id);
    }

    @Test
    void guardar_devuelveTareaGuardada() {

        Tarea tareaNueva = new Tarea();
        Tarea tareaGuardada = new Tarea();

        when(tareaRepository.save(tareaNueva)).thenReturn(tareaGuardada);

        Tarea resultado = tareaService.guardar(tareaNueva);

        assertEquals(tareaGuardada, resultado);
        verify(tareaRepository).save(tareaNueva);
    }

    @Test
    void eliminar_cuandoExiste_eliminarTarea() {

        Long id = 1L;

        when(tareaRepository.existsById(id)).thenReturn(true);

        tareaService.eliminar(id);

        verify(tareaRepository).existsById(id);
        verify(tareaRepository).deleteById(id);
    }

    @Test
    void eliminar_cuandoNoexiste_lanzaExcepcion() {

        Long id = 99L;

        when(tareaRepository.existsById(id)).thenReturn(false);

        RecursoNoEncontradoException excepcion = assertThrows(RecursoNoEncontradoException.class,
                () -> tareaService.eliminar(id));

        assertEquals("Tarea no encontrada con ID: " + id, excepcion.getMessage());

        verify(tareaRepository).existsById(id);
        verify(tareaRepository, never()).deleteById(id);
    }

    @Test
    void actualizar_cuandoExiste_actualizaYDevuelveTarea() {

        Long id = 1L;

        Tarea tareaExistente = new Tarea();
        tareaExistente.setTitulo("Título anterior");
        tareaExistente.setDescripcion("Descripción anterior");
        tareaExistente.setEstado("PENDIENTE");

        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setTitulo("Título actualizado");
        tareaActualizada.setDescripcion("Descripción actualizada");
        tareaActualizada.setEstado("COMPLETADA");

        when(tareaRepository.findById(id)).thenReturn(Optional.of(tareaExistente));

        when(tareaRepository.save(tareaExistente)).thenReturn(tareaExistente);

        Tarea resultado = tareaService.actualizar(id, tareaActualizada);

        assertEquals("Título actualizado", resultado.getTitulo());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        assertEquals("COMPLETADA", resultado.getEstado());

        verify(tareaRepository).findById(id);
        verify(tareaRepository).save(tareaExistente);
    }

    @Test
    void actualizar_cuandoNoExiste_lanzaExcepcion() {

        Long id = 99L;
        Tarea tareaActualizada = new Tarea();

        tareaActualizada.setTitulo("Titulo actualizado");
        tareaActualizada.setDescripcion("Dscripción actualizada");
        tareaActualizada.setEstado("COMPLETA");

        when(tareaRepository.findById(id)).thenReturn(Optional.empty());

        RecursoNoEncontradoException excepcion = assertThrows(RecursoNoEncontradoException.class,
                () -> tareaService.actualizar(id, tareaActualizada));

        assertEquals("Tarea no encontrada con ID: " + id, excepcion.getMessage());

        verify(tareaRepository).findById(id);
        verify(tareaRepository, never()).save(tareaActualizada);
    }
}
