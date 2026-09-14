package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.dto.UsuarioDTO;
import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import jakarta.validation.Valid;

@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
            UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    @GetMapping("/dto/{id}")
    public UsuarioDTO obtenerUsuarioDTO(
            @PathVariable Long id) {

        Usuario usuario = usuarioService.obtenerPorId(id);

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail());
    }

    @Operation(summary = "Obtiene todos los usuarios")
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.obtenerTodos();
    }

    @Operation(summary = "Obtener usuarios por ID")
    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(
            @PathVariable Long id) {

        return usuarioService.obtenerPorId(id);
    }

    @Operation(summary = "Crear nuevo usuario")
    @PostMapping
    public Usuario createUsuario(

            @Valid @RequestBody Usuario usuario) {

        return usuarioService.guardar(usuario);
    }

    @Operation(summary = "Actualizar usuario existente")
    @PutMapping("/{id}")
    public Usuario actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuarioActualizado) {

        return usuarioService.actualizar(
                id,
                usuarioActualizado);
    }

    @Operation(summary = "Eliminar Usuario")
    @DeleteMapping("/{id}")
    public void eliminarUsuario(
            @PathVariable Long id) {

        usuarioService.eliminar(id);
    }

}