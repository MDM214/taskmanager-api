package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.dto.UsuarioDTO;
import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.service.UsuarioService;
import com.manuel.taskmanager.dto.UsuarioDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

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

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(
            @PathVariable Long id) {

        return usuarioService.obtenerPorId(id);
    }

    @PostMapping
    public Usuario createUsuario(

            @Valid @RequestBody Usuario usuario) {

        return usuarioService.guardar(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuarioActualizado) {

        return usuarioService.actualizar(
                id,
                usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(
            @PathVariable Long id) {

        usuarioService.eliminar(id);
    }

}