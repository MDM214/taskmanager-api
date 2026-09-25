package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.dto.LoginRequestDTO;
import com.manuel.taskmanager.dto.LoginResponseDTO;
import jakarta.validation.Valid;
import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.repository.UsuarioRepository;
import com.manuel.taskmanager.service.JwtService;
import com.manuel.taskmanager.exception.CredencialesInvalidasException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

        private final JwtService jwtService;
        private final UsuarioRepository usuarioRepository;
        private final PasswordEncoder passwordEncoder;

        public AuthController(
                        JwtService jwtService,
                        UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder) {

                this.jwtService = jwtService;
                this.usuarioRepository = usuarioRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @PostMapping("/login")
        public LoginResponseDTO login(
                        @Valid @RequestBody LoginRequestDTO request) {

                Usuario usuario = usuarioRepository
                                .findByEmail(request.getEmail())
                                .orElseThrow(() -> new CredencialesInvalidasException(
                                                "Usuario o contraseña no encontrado"));

                if (!passwordEncoder.matches(
                                request.getPassword(),
                                usuario.getPassword())) {

                        throw new CredencialesInvalidasException(
                                        "Usuario o Contraseña incorrecta");
                }

                String token = jwtService
                                .generarToken(usuario.getEmail());

                return new LoginResponseDTO(token);
        }

        @GetMapping("/whoami")
        public String whoAmi() {

                return SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();
        }

}