package com.manuel.taskmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach 
    void setUp() {

        String secret = "clave-secreta-de.prueba-con-mas-de-32-caracteres";

        long expiration = 60000L;

        jwtService = new JwtService(secret, expiration);
    }

    @Test 
    void generarToken_devuelveTokenNoVacio() {

        String username = "manuel@example.com";

        String token = jwtService.generarToken(username);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test 
    void extraerUsername_devuelveUsernameCorrecto() {

        String username = "manuel@example.com";

        String token = jwtService.generarToken(username);

        String usernameExtraido = jwtService.extraerUsername(token);

        assertEquals(username, usernameExtraido);
    }

    @Test 
    void validarToken_cuandoEsValido_devuelvaTrue() {

        String username = "manuel@example.com";

        String token = jwtService.generarToken(username);

        boolean resultado = jwtService.validarToken(token);

        assertTrue(resultado);
    }

    @Test 
    void validarToken_cuandoEstaAlterado_devuelveFalse() {

        String username = "manuel@example.com";

        String token = jwtService.generarToken(username);

        char ultimoCaracter = token.charAt(token.length() -1);

        char nuevoUltimoCaracter = ultimoCaracter == 'a' ? 'b' : 'a';

        String tokenAlterado = token.substring(0, token.length() - 1) + nuevoUltimoCaracter;

        boolean resultado = jwtService.validarToken(tokenAlterado);

        assertFalse(resultado);
    }

    @Test 
    void validarToken_cuandoEstaExpirado_devuelveFalse() {

        String secret = "clave-secreta-de-prueba-con-mas-de-32-caracteres";

        JwtService jwtServiceConTokenExpirado = new JwtService(secret, -1000L);

        String tokenExpirado = jwtServiceConTokenExpirado.generarToken("manuel@example.com");

        boolean resultado = jwtServiceConTokenExpirado.validarToken(tokenExpirado);
        assertFalse(resultado);
    }


}
