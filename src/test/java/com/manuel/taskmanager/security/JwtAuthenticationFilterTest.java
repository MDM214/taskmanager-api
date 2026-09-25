package com.manuel.taskmanager.security;

import com.manuel.taskmanager.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {

        SecurityContextHolder.clearContext();
    }

    @Test
    void sinAuthorization_continuaSinAutenticar()
            throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(
                request,
                response,
                filterChain);

        assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication());

        verify(request).getHeader("Authorization");
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void authorizationSinBearer_continuaSinAutenticar()
            throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn("Basic credenciales");

        jwtAuthenticationFilter.doFilterInternal(
                request,
                response,
                filterChain);

        assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication());

        verify(request).getHeader("Authorization");
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void tokenInvalido_continuaSinAutenticar()
            throws Exception {

        String token = "token-invalido";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtService.validarToken(token))
                .thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(
                request,
                response,
                filterChain);

        assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication());

        verify(request).getHeader("Authorization");
        verify(jwtService).validarToken(token);
        verify(jwtService, never()).extraerUsername(token);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void tokenValido_estableceAutenticacion()
            throws Exception {

        String token = "token-valido";
        String username = "manuel@example.com";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtService.validarToken(token))
                .thenReturn(true);

        when(jwtService.extraerUsername(token))
                .thenReturn(username);

        jwtAuthenticationFilter.doFilterInternal(
                request,
                response,
                filterChain);

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        assertNotNull(authentication);
        assertEquals(username, authentication.getName());
        assertNull(authentication.getCredentials());
        assertEquals(0, authentication.getAuthorities().size());

        verify(request).getHeader("Authorization");
        verify(jwtService).validarToken(token);
        verify(jwtService).extraerUsername(token);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void autenticacionExistente_noSeSustituye()
            throws Exception {

        String token = "token-valido";
        String usernameExistente = "usuario-existente@example.com";

        UsernamePasswordAuthenticationToken autenticacionExistente = new UsernamePasswordAuthenticationToken(
                usernameExistente,
                null,
                java.util.List.of());

        SecurityContextHolder
                .getContext()
                .setAuthentication(autenticacionExistente);

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtService.validarToken(token))
                .thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(
                request,
                response,
                filterChain);

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        assertNotNull(authentication);

        assertEquals(
                usernameExistente,
                authentication.getName());

        verify(request).getHeader("Authorization");
        verify(jwtService).validarToken(token);
        verify(jwtService, never()).extraerUsername(token);
        verify(filterChain).doFilter(request, response);
    }
}
