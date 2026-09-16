package com.manuel.taskmanager.security;

import com.manuel.taskmanager.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter
                extends OncePerRequestFilter {

        private final JwtService jwtService;

        public JwtAuthenticationFilter(
                        JwtService jwtService) {

                this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain)

                        throws ServletException, IOException {

                String authHeader = request.getHeader("Authorization");
                System.out.println("Authorization Header = " + authHeader);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {

                        String token = authHeader.substring(7);
                        if (jwtService.validarToken(token)) {

                                String username = jwtService.extraerUsername(token);
                                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                                username,
                                                null,
                                                java.util.Collections.emptyList());

                                SecurityContextHolder
                                                .getContext()
                                                .setAuthentication(authToken);

                                System.out.println(
                                                "Usuario autenticado: "
                                                                + username);
                        }
                }
                filterChain.doFilter(
                                request,
                                response);
        }

        @GetMapping("/whoami")
        public String whoami(
                        HttpServletRequest request) {

                return request.getHeader("Authorization");
        }
}
