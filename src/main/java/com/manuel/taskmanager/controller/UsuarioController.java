package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
            UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
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
            @RequestBody Usuario usuario) {

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