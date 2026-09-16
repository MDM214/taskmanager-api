package com.manuel.taskmanager.controller;

import com.manuel.taskmanager.dto.LoginRequestDTO;
import com.manuel.taskmanager.dto.LoginResponseDTO;
import com.manuel.taskmanager.entity.Usuario;
import com.manuel.taskmanager.repository.UsuarioRepository;
import com.manuel.taskmanager.service.JwtService;

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
                        @RequestBody LoginRequestDTO request) {

                Usuario usuario = usuarioRepository
                                .findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException(
                                                "Usuario no encontrado"));

                if (!passwordEncoder.matches(
                                request.getPassword(),
                                usuario.getPassword())) {

                        throw new RuntimeException(
                                        "Contraseña incorrecta");
                }

                String token = jwtService
                                .generarToken(usuario.getEmail());

                return new LoginResponseDTO(token);
        }

        @GetMapping("/test")
        public String test() {

                String token = jwtService.generarToken("Manuel");

                return jwtService.extraerUsername(token);
        }

        @GetMapping("/whoami")
        public String whoAmi() {

                return SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();
        }

        @GetMapping("/token-test")
        public String tokenTest(
                        @RequestHeader("Autorization") String authHeader) {

                return authHeader;
        }
}