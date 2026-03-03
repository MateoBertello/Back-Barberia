package com.example.bareberiaapi.controller;

import com.example.bareberiaapi.dto.AuthResponse;
import com.example.bareberiaapi.dto.LoginRequest;
import com.example.bareberiaapi.entity.Usuario;
import com.example.bareberiaapi.repository.UsuarioRepository;
import com.example.bareberiaapi.security.JwtUtil;
import com.example.bareberiaapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permite que React se conecte
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    // --- RUTA 1: INICIAR SESIÓN ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Usuario no encontrado\"}");
        }

        Usuario usuario = usuarioOpt.get();

        // Comparamos usando la licuadora profesional BCrypt
        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Contraseña incorrecta\"}");
        }

        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());
        AuthResponse response = new AuthResponse(token, usuario.getRol(), usuario.getId(), usuario.getNombre());

        return ResponseEntity.ok(response);
    }

    // --- RUTA 2: REGISTRAR NUEVO USUARIO ---
    @PostMapping("/register")
    public ResponseEntity<?> registrarNuevoCliente(@RequestBody Usuario nuevoUsuario) {
        try {
            // Forzamos el rol para que nadie pueda hacerse Administrador desde afuera
            nuevoUsuario.setRol("CLIENTE");
            nuevoUsuario.setActivo(true);

            // Usamos tu servicio que ya tiene BCrypt configurado
            Usuario usuarioCreado = usuarioService.guardar(nuevoUsuario);

            return ResponseEntity.ok(usuarioCreado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Error al crear usuario. ¿El email ya existe?\"}");
        }
    }
}