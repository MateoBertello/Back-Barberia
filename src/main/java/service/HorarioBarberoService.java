package service;

import entity.HorarioBarbero;
import repository.HorarioBarberoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HorarioBarberoService {
    @Autowired
    private HorarioBarberoRepository horarioRepository;

    public List<HorarioBarbero> listarTodos() { return horarioRepository.findAll(); }
    public HorarioBarbero guardar(HorarioBarbero horario) { return horarioRepository.save(horario); }
}