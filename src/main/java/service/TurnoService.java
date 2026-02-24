package service;

import entity.Turno;
import repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    public Turno crearTurno(Turno turno) {

        // 1. Validar que el barbero no tenga otro turno a la misma hora
        boolean ocupado = turnoRepository.existsByBarberoIdAndFechaHoraInicio(
                turno.getBarbero().getId(),
                turno.getFechaHoraInicio()
        );

        if (ocupado) {
            throw new RuntimeException("El barbero ya tiene un turno reservado en ese horario.");
        }

        // 2. Si pasa la validación, guardamos el turno
        return turnoRepository.save(turno);
    }
}