package com.manuel.taskmanager.service;

import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.repository.UsuarioRepository;
import com.manuel.taskmanager.exception.RecursoNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void obtenerTodos_devuelveListaDeUsuarios() {

        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();

        List<Usuario> usuarioEsperados = List.of(usuario1, usuario2);

        when(usuarioRepository.findAll()).thenReturn(usuarioEsperados);

        List<Usuario> resultado = usuarioService.obtenerTodos();

        assertEquals(usuarioEsperados, resultado);
        verify(usuarioRepository).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_devuelveUsuario() {

        Long id = 1L;
        Usuario usuarioEsperado = new Usuario();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEsperado));

        Usuario resultado = usuarioService.obtenerPorId(id);

        assertEquals(usuarioEsperado, resultado);
        verify(usuarioRepository).findById(id);
    }

    @Test

void obtenerPorId_cuandoNoExiste_lanzaExcepcion() {

    Long id = 99L;

    when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

    RecursoNoEncontradoException excepcion = assertThrows(
            RecursoNoEncontradoException.class,
            () -> usuarioService.obtenerPorId(id));

    assertEquals(
        "Usuario no encontrado con ID: " + id, excepcion.getMessage());

    verify(usuarioRepository).findById(id);
    }

    @Test 
    void guardar_codificaPasswordAsignaRolYDevuelveUsuario() {

        Usuario usuario = new Usuario();
        usuario.setNombre("Manuel");
        usuario.setEmail("manuel@example.com");
        usuario.setPassword("passwordOriginal");

        String passwordCodificada = "passwordCodificada";

        when(passwordEncoder.encode("passwordOriginal"))
                .thenReturn(passwordCodificada);

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        Usuario resultado = usuarioService.guardar(usuario);

        assertEquals(passwordCodificada, resultado.getPassword());
        assertEquals("USER", resultado.getRol());

        verify(passwordEncoder).encode("passwordOriginal");
        verify(usuarioRepository).save(usuario);

    }

    @Test 
    void eliminar_cuandoExiste_eliminaUsuario() {

        Long id = 1L;

        when(usuarioRepository.existsById(id)).thenReturn(true);

        usuarioService.eliminar(id);

        verify(usuarioRepository).existsById(id);
        verify(usuarioRepository).deleteById(id);
    }

    @Test 
    void eliminar_cuandoNoExiste_lanzaExcepcion() {

        Long id = 99L;

        when(usuarioRepository.existsById(id)).thenReturn(false);

        RecursoNoEncontradoException excepcion = assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.eliminar(id));

        assertEquals("Usuario no encontrado con ID: " + id, excepcion.getMessage());

        verify(usuarioRepository).existsById(id);
        verify(usuarioRepository, never()).deleteById(id);
    }

    @Test 
    void actualizar_cuandoExiste_actualizaYDevuelveUsuario() {

        Long id = 1L;

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setNombre("Nombre anterior");
        usuarioExistente.setEmail("anterior@example.com");
        usuarioExistente.setPassword("passwordCodificada");
        usuarioExistente.setRol("USER");

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Nombre actualizado");
        usuarioActualizado.setEmail("actualizado@example.com");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));

        when(usuarioRepository.save(usuarioExistente)).thenReturn(usuarioExistente);

        Usuario resultado = usuarioService.actualizar(id, usuarioActualizado);

        assertEquals("Nombre actualizado", resultado.getNombre());

        assertEquals("actualizado@example.com", resultado.getEmail());

        assertEquals("passwordCodificada", resultado.getPassword());

        assertEquals("USER", resultado.getRol());

        verify(usuarioRepository).findById(id);
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test 
    void actualizar_cuandoNoExiste_lanzaExcepcion() {

        Long id = 99L;

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Nombre actualizado");
        usuarioActualizado.setEmail("actualizado@example.com");

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        RecursoNoEncontradoException excepcion = assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.actualizar(id, usuarioActualizado));

        assertEquals("Usuario no encontrado con ID: " + id, excepcion.getMessage());

        verify(usuarioRepository).findById(id);
        verify(usuarioRepository, never()).save(usuarioActualizado);
    }
}