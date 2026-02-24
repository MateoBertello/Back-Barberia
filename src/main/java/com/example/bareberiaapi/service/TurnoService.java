package com.example.bareberiaapi.service;

import com.example.bareberiaapi.entity.HorarioBarbero;
import com.example.bareberiaapi.entity.Turno;
import com.example.bareberiaapi.repository.HorarioBarberoRepository;
import com.example.bareberiaapi.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private HorarioBarberoRepository horarioBarberoRepository;

    public Turno crearTurno(Turno turno) {

        // 1. VALIDACIÓN: Que el turno no sea en el pasado
        if (turno.getFechaHoraInicio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No puedes reservar un turno en el pasado.");
        }

        // 2. VALIDACIÓN: Que el barbero trabaje ese día y en ese horario
        // Extraemos el día de la semana (MONDAY, TUESDAY, etc.) y la hora
        String diaSemana = turno.getFechaHoraInicio().getDayOfWeek().name();
        LocalTime horaTurno = turno.getFechaHoraInicio().toLocalTime();

        // Buscamos si el barbero tiene cargado un horario para ese día
        HorarioBarbero horario = horarioBarberoRepository
                .findByBarberoIdAndDiaSemana(turno.getBarbero().getId(), diaSemana)
                .orElseThrow(() -> new RuntimeException("El barbero no trabaja el día " + diaSemana + "."));

        // Comprobamos si la hora del turno está dentro de la hora de inicio y fin
        if (horaTurno.isBefore(horario.getHoraInicio()) || horaTurno.isAfter(horario.getHoraFin())) {
            throw new RuntimeException("El turno está fuera del horario laboral del barbero (Trabaja de "
                    + horario.getHoraInicio() + " a " + horario.getHoraFin() + ").");
        }

        // 3. VALIDACIÓN (La que ya tenías): Que el barbero no tenga otro turno superpuesto
        boolean ocupado = turnoRepository.existsByBarberoIdAndFechaHoraInicio(
                turno.getBarbero().getId(),
                turno.getFechaHoraInicio()
        );

        if (ocupado) {
            throw new RuntimeException("El barbero ya tiene un turno reservado en ese horario exacto.");
        }

        // 4. GUARDADO
        return turnoRepository.save(turno);
    }
}