package com.example.bareberiaapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    //guardian
    @Autowired
    private JwtFilter jwtFilter;

    // 1. EL ENCRIPTADOR DE CONTRASEÑAS
    // Le decimos a Spring que cuando necesite encriptar algo, use BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. LAS REGLAS DE LA PUERTA (Por ahora)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactivamos protección de formularios HTML

                // Apagamos el sistema de "Sesiones" (porque los Tokens son Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 1. LAS RUTAS PÚBLICAS: Cualquiera puede intentar hacer Login
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2. EL CANDADO GENERAL: Toda otra ruta (/api/turnos, /api/sucursales, etc) REQUIERE TOKEN
                        .anyRequest().authenticated()
                )
                // Ponemos a nuestro Guardia (JwtFilter) justo en la puerta de entrada
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}