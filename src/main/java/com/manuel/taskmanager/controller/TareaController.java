package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.dto.TareaDTO;
import com.manuel.taskmanager.entity.Tarea;
import com.manuel.taskmanager.service.TareaService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Tarea", description = "Operaciones relacionadas con las tareas")
@RestController
@RequestMapping("/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @Operation(summary = "Obtener todas las tareas dto")
    @GetMapping("/dto/{id}")
    public TareaDTO obtenerTareaDTO(
            @PathVariable Long id) {

        Tarea tarea = tareaService.obtenerPorId(id);

        return new TareaDTO(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getEstado());
    }

    @Operation(summary = "Obtener todas las tareas")
    @GetMapping
    public List<Tarea> obtenerTodas() {
        return tareaService.obtenerTodas();
    }

    @Operation(summary = "Obtener una tarea por ID")
    @GetMapping("/{id}")
    public Tarea obtenerPorId(
            @PathVariable Long id) {

        return tareaService.obtenerPorId(id);
    }

    @Operation(summary = "Crear nueva tarea")
    @PostMapping
    public Tarea crearTarea(
            @RequestBody Tarea tarea) {

        return tareaService.guardar(tarea);
    }

    @Operation(summary = "Actualizar tarea")
    @PutMapping("/{id}")
    public Tarea actualizar(
            @PathVariable Long id,
            @RequestBody Tarea tarea) {

        return tareaService.actualizar(id, tarea);
    }

    @Operation(summary = "Eliminar tarea")
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        tareaService.eliminar(id);
    }
}