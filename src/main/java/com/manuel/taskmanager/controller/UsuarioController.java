package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.dto.CrearUsuarioDTO;
import com.manuel.taskmanager.dto.UsuarioDTO;
import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
            UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public List<UsuarioDTO> getAllUsuarios() {

        return usuarioService.obtenerTodos()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public UsuarioDTO obtenerUsuarioPorId(
            @PathVariable Long id) {

        Usuario usuario = usuarioService.obtenerPorId(id);

        return convertirADTO(usuario);
    }

    @Operation(summary = "Crear nuevo usuario")
    @PostMapping
    public UsuarioDTO crearUsuario(
            @Valid @RequestBody CrearUsuarioDTO dto) {

        Usuario usuario = new Usuario();

        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());

        Usuario usuarioGuardado = usuarioService.guardar(usuario);

        return convertirADTO(usuarioGuardado);
    }

    @Operation(summary = "Actualizar usuario existente")
    @PutMapping("/{id}")
    public UsuarioDTO actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuarioActualizado) {
        Usuario usuario = usuarioService.actualizar(id, usuarioActualizado);

        return convertirADTO(usuario);
    }

    @Operation(summary = "Eliminar Usuario")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(
            @PathVariable Long id) {

        usuarioService.eliminar(id);
    }

    private UsuarioDTO convertirADTO(
            Usuario usuario) {

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail());
    }

}