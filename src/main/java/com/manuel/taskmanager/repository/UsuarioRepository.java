package com.manuel.taskmanager.repository;

import com.manuel.taskmanager.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    // boolean existsByEmail(String email);

}