package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.dto.CrearTareaDTO;
import com.manuel.taskmanager.dto.TareaDTO;
import com.manuel.taskmanager.entity.Tarea;
import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.service.TareaService;
import com.manuel.taskmanager.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tareas", description = "Operaciones relacionadas con las tareas")
@RestController
@RequestMapping("/tareas")
public class TareaController {

    private final TareaService tareaService;
    private final UsuarioService usuarioService;

    public TareaController(
            TareaService tareaService,
            UsuarioService usuarioService) {

        this.tareaService = tareaService;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Obtener todas las tareas")
    @GetMapping
    public List<TareaDTO> obtenerTodas() {

        return tareaService.obtenerTodas()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Operation(summary = "Obtener una tarea por ID")
    @GetMapping("/{id}")
    public TareaDTO obtenerPorId(
            @PathVariable Long id) {

        Tarea tarea = tareaService.obtenerPorId(id);

        return convertirADTO(tarea);
    }

    @Operation(summary = "Crear nueva tarea")
    @PostMapping
    public TareaDTO crearTarea(
            @Valid @RequestBody CrearTareaDTO dto) {

        Usuario usuario = usuarioService.obtenerPorId(
                dto.getUsuarioId());

        Tarea tarea = new Tarea();

        tarea.setTitulo(dto.getTitulo());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setEstado(dto.getEstado());
        tarea.setUsuario(usuario);

        Tarea tareaGuardada = tareaService.guardar(tarea);

        return convertirADTO(tareaGuardada);
    }

    @Operation(summary = "Actualizar tarea")
    @PutMapping("/{id}")
    public TareaDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Tarea tarea) {

        Tarea tareaActualizada = tareaService.actualizar(
                id,
                tarea);

        return convertirADTO(tareaActualizada);
    }

    @Operation(summary = "Eliminar tarea")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable Long id) {

        tareaService.eliminar(id);
    }

    private TareaDTO convertirADTO(
            Tarea tarea) {

        return new TareaDTO(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getEstado());
    }
}