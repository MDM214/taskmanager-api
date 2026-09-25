package com.manuel.taskmanager.service;

import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) class CustomUserDetailsServiceTest {

    @Mock 
    private UsuarioRepository usuarioRepository;

    @InjectMocks 
    private CustomUserDetailsService customUserDetailsService;

    @Test 
    void loadUserByUsername_cuandoExiste_devuelveUserDetails() {

        String email = "manuel@example.com";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword("passwordCodificada");
        usuario.setRol("USER");

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        UserDetails resultado = customUserDetailsService.loadUserByUsername(email);

        assertEquals(email, resultado.getUsername());

        assertEquals("passwordCodificada", resultado.getPassword());

        assertTrue(resultado.getAuthorities().stream().anyMatch(autoridad -> autoridad.getAuthority().equals("ROLE_USER")));

        verify(usuarioRepository).findByEmail(email);
    }

    @Test 
    void loadUserByUsername_cuandoNoExiste_lanzaExcepcion(){

        String email = "inexistente@example.com";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException excepcion = assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(email));

        assertEquals("Usuario no encontrado", excepcion.getMessage());

        verify(usuarioRepository).findByEmail(email);
    }
}
