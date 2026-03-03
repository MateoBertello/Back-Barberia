package com.example.bareberiaapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 1. LA FIRMA SECRETA: Esta clave es el corazón de tu seguridad.
    // Es lo que impide que un hacker invente sus propios tokens. (Debe tener al menos 256 bits / 32 caracteres)
    private static final String SECRET = "EstaEsUnaLlaveSecretaSuperSeguraParaBarberSaaS2026!";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 2. TIEMPO DE VIDA: El token durará 24 horas (en milisegundos)
    private static final long EXPIRATION_TIME = 86400000;

    // --- MÉTODO PARA FABRICAR EL TOKEN ---
    public String generarToken(String email, String rol) {
        return Jwts.builder()
                .setSubject(email) // El "dueño" del token
                .claim("rol", rol) // Guardamos el rol adentro de la pulsera
                .setIssuedAt(new Date()) // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Cuándo caduca
                .signWith(key, SignatureAlgorithm.HS256) // Lo firmamos criptográficamente
                .compact();
    }

    // --- MÉTODO PARA LEER EL TOKEN ---
    public String extraerEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // --- MÉTODO PARA VERIFICAR SI EL TOKEN ES FALSO O ESTÁ VENCIDO ---
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true; // Es auténtico
        } catch (Exception e) {
            return false; // Alguien lo manipuló o ya caducó
        }
    }
}