package com.manuel.taskmanager.service;

import com.manuel.taskmanager.entity.Tarea;
import com.manuel.taskmanager.repository.TareaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public List<Tarea> obtenerTodas() {
        return tareaRepository.findAll();
    }

    public Tarea obtenerPorId(Long id) {
        return tareaRepository.findById(id).orElse(null);
    }

    public Tarea guardar(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    public void eliminar(Long id) {
        tareaRepository.deleteById(id);
    }

    public Tarea actualizar(Long id, Tarea tareaActualizada) {
        Tarea tarea = tareaRepository.findById(id).orElse(null);

        if (tarea != null) {

            tarea.setTitulo(tareaActualizada.getTitulo());
            tarea.setDescripcion(tareaActualizada.getDescripcion());
            tarea.setEstado(tareaActualizada.getEstado());

            return tareaRepository.save(tarea);
        }

        return null;
    }

}
