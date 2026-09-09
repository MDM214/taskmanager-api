package com.manuel.taskmanager.repository;

import com.manuel.taskmanager.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

}