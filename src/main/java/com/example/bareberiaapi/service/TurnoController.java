package com.example.bareberiaapi.service;

import com.example.bareberiaapi.entity.Turno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {

    @Autowired
    private TurnoService turnoService;

    @PostMapping
    public ResponseEntity<?> reservarTurno(@RequestBody Turno turno) {
        try {
            Turno nuevoTurno = turnoService.crearTurno(turno);
            return ResponseEntity.ok(nuevoTurno);
        } catch (RuntimeException e) {
            // Si el barbero está ocupado, devolvemos un Error 400 (Bad Request) con el mensaje
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}