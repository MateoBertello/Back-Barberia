package com.example.bareberiaapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Miramos si la petición trae el encabezado "Authorization"
        String authHeader = request.getHeader("Authorization");

        // 2. Comprobamos si el encabezado existe y si empieza con "Bearer " (que significa "Portador del Token")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // 3. Le quitamos los primeros 7 caracteres ("Bearer ") para quedarnos solo con el token puro
            String token = authHeader.substring(7);

            // 4. Le preguntamos a la fábrica si este token es real y no está vencido
            if (jwtUtil.validarToken(token)) {

                // 5. Si es válido, sacamos el email del dueño
                String email = jwtUtil.extraerEmail(token);

                // 6. Le avisamos a Spring Security: "Anotá que este usuario ya mostró su credencial, dejalo pasar"
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 7. Sigue tu camino (Si no traía token válido, llegará sin autenticación y Spring lo rebotará)
        filterChain.doFilter(request, response);
    }
}