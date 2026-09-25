package com.manuel.taskmanager.service;

import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.exception.RecursoNoEncontradoException;
import com.manuel.taskmanager.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository
                .findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con ID: " + id));
    }

    public Usuario guardar(Usuario usuario) {

        usuario.setPassword(
                passwordEncoder.encode(
                        usuario.getPassword()));

        usuario.setRol("USER");

        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Usuario no encontrado con ID: " + id);
        }

        usuarioRepository.deleteById(id);
    }

    public Usuario actualizar(Long id,
            Usuario usuarioActualizado) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con ID: " + id));

        usuario.setNombre(
                usuarioActualizado.getNombre());

        usuario.setEmail(
                usuarioActualizado.getEmail());

        return usuarioRepository.save(usuario);
    }
}