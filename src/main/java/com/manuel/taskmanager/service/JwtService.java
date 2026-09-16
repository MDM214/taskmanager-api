package com.manuel.taskmanager.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

        private final SecretKey secretKey = Keys.hmacShaKeyFor(
                        "mi_clave_super_secreta_para_taskmanager_2026"
                                        .getBytes());

        public String generarToken(String username) {

                return Jwts.builder()
                                .subject(username)
                                .issuedAt(new Date())
                                .expiration(

                                                new Date(System.currentTimeMillis()
                                                                + 3600000))
                                .signWith(secretKey)
                                .compact();
        }

        public String extraerUsername(String token) {

                return Jwts.parser()
                                .verifyWith(secretKey)
                                .build()
                                .parseSignedClaims(token)
                                .getPayload()
                                .getSubject();
        }

        public boolean validarToken(String token) {

                try {

                        Jwts.parser()
                                        .verifyWith(secretKey)
                                        .build()
                                        .parseSignedClaims(token);

                        return true;

                } catch (Exception e) {

                        return false;
                }
        }
}
