package com.manuel.taskmanager.service;

import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario actualizar(Long id,
            Usuario usuarioActualizado) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElse(null);

        if (usuario != null) {

            usuario.setNombre(
                    usuarioActualizado.getNombre());

            usuario.setEmail(
                    usuarioActualizado.getEmail());

            return usuarioRepository.save(usuario);
        }

        return null;
    }
}