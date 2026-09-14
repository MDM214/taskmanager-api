package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.dto.TareaDTO;
import com.manuel.taskmanager.entity.Tarea;
import com.manuel.taskmanager.service.TareaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping("/dto/{id}")
    public TareaDTO obtenerTareaDTO(
            @PathVariable Long id) {

        Tarea tarea = tareaService.obtenerPorId(id);

        return new TareaDTO(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getEstado());
    }

    @GetMapping
    public List<Tarea> obtenerTodas() {
        return tareaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Tarea obtenerPorId(
            @PathVariable Long id) {

        return tareaService.obtenerPorId(id);
    }

    @PostMapping
    public Tarea crearTarea(
            @RequestBody Tarea tarea) {

        return tareaService.guardar(tarea);
    }

    @PutMapping("/{id}")
    public Tarea actualizar(
            @PathVariable Long id,
            @RequestBody Tarea tarea) {

        return tareaService.actualizar(id, tarea);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        tareaService.eliminar(id);
    }
}